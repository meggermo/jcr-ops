package nl.meg.jcr.internal;

import nl.meg.function.EFunction;
import nl.meg.jcr.*;

import javax.jcr.*;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import java.util.Optional;

import static nl.meg.function.FunctionSupport.relax;

abstract class AbstractHippoItem<E extends Item> implements HippoItem<E> {

    private final E item;
    private final HippoEntityFactory hippoEntityFactory = new HippoEntityFactoryImpl();

    AbstractHippoItem(E item) {
        this.item = item;
    }

    @Override
    public final E get() {
        return item;
    }

    protected final HippoNode node(Node node) {
        return hippoEntityFactory.node(node);
    }

    protected final HippoProperty property(Property property) {
        return hippoEntityFactory.property(property);
    }

    protected final HippoValue value(Value value) {
        return hippoEntityFactory.value(value);
    }

    protected final HippoVersion version(Version version) {
        return hippoEntityFactory.version(version);
    }

    protected final HippoVersionHistory versionHistory(VersionHistory versionHistory) {
        return hippoEntityFactory.versionHistory(versionHistory);
    }

    @Override
    public Optional<HippoNode> getParent() {
        return Optional.ofNullable(
                invoke(n -> {
                    try {
                        return node(n.getParent());
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
