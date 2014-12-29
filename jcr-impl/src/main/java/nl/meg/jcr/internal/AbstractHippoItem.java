package nl.meg.jcr.internal;

import nl.meg.function.EFunction;
import nl.meg.jcr.HippoEntityFactory;
import nl.meg.jcr.HippoItem;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.RuntimeRepositoryException;

import javax.jcr.Item;
import javax.jcr.ItemNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Optional;

import static nl.meg.function.FunctionSupport.relax;

abstract class AbstractHippoItem<E extends Item> implements HippoItem<E> {

    private final E item;
    private final HippoEntityFactory hippoEntityFactory;

    AbstractHippoItem(E item, HippoEntityFactory hippoEntityFactory) {
        this.item = item;
        this.hippoEntityFactory = hippoEntityFactory;
    }

    @Override
    public final E get() {
        return item;
    }

    protected final HippoEntityFactory factory() {
        return hippoEntityFactory;
    }

    @Override
    public Optional<HippoNode> getParent() {
        return Optional.ofNullable(
                invoke(n -> {
                    try {
                        return factory().node(n.getParent());
                    } catch (ItemNotFoundException e) {
                        return null;
                    }
                })
        );
    }

    @Override
    public String getName() {
        return invoke(Item::getName);
    }

    @Override
    public String getPath() {
        return invoke(Item::getPath);
    }

    @Override
    public Session getSession() {
        return invoke(Item::getSession);
    }

    @Override
    public int getDepth() {
        return invoke(Item::getDepth);
    }

    @Override
    public boolean isModified() {
        return invoke(Item::isModified);
    }

    @Override
    public boolean isNew() {
        return invoke(Item::isNew);
    }

    @Override
    public boolean isNode() {
        return invoke(Item::isNode);
    }

    @Override
    public boolean isSame(HippoItem<E> other) {
        return invoke(i -> i.isSame(other.get()));
    }

    @Override
    public Item getAncestor(int depth) {
        return invoke(i -> i.getAncestor(depth));
    }

    protected final <T> T invoke(EFunction<E, T, RepositoryException> f) {
        return relax(f, get(), RuntimeRepositoryException::new);
    }
}
