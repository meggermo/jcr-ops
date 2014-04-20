package nl.meg.jcr.internal;

import nl.meg.jcr.HippoItem;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.HippoValue;
import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.*;
import java.util.Optional;
import java.util.function.Function;

import static nl.meg.function.FunctionAdapter.relax;

abstract class AbstractHippoItem<E extends Item> implements HippoItem<E> {

    private final E node;
    private final Function<Node, HippoNode> hippoNodeFactory;
    private final Function<Property, HippoProperty> hippoPropertyFactory;
    private final Function<Value, HippoValue> hippoValueFactory;

    AbstractHippoItem(E node) {
        this.node = node;
        this.hippoNodeFactory = HippoNodeImpl::new;
        this.hippoPropertyFactory = HippoPropertyImpl::new;
        this.hippoValueFactory = HippoValueImpl::new;
    }

    @Override
    public final E get() {
        return node;
    }

    protected final HippoNode node(Node node) {
        return hippoNodeFactory.apply(node);
    }

    protected final HippoProperty property(Property property) {
        return hippoPropertyFactory.apply(property);
    }

    protected final HippoValue value(Value value) {
        return hippoValueFactory.apply(value);
    }

    @Override
    public Optional<HippoNode> getParent() {
        return Optional.ofNullable(
                relax(n -> {
                    try {
                        return node(n.getParent());
                    } catch (ItemNotFoundException e) {
                        return null;
                    }
                }, get(), RuntimeRepositoryException::new)
        );
    }

    @Override
    public String getName() {
        return relax(Item::getName, get(), RuntimeRepositoryException::new);
    }

    @Override
    public String getPath() {
        return relax(Item::getPath, get(), RuntimeRepositoryException::new);
    }

    @Override
    public Session getSession() {
        return relax(Item::getSession, get(), RuntimeRepositoryException::new);
    }

    @Override
    public int getDepth() {
        return relax(Item::getDepth, get(), RuntimeRepositoryException::new);
    }

    @Override
    public boolean isModified() {
        return relax(Item::isModified, get(), RuntimeRepositoryException::new);
    }

    @Override
    public boolean isNew() {
        return relax(Item::isNew, get(), RuntimeRepositoryException::new);
    }

    @Override
    public boolean isNode() {
        return relax(Item::isNode, get(), RuntimeRepositoryException::new);
    }

    @Override
    public boolean isSame(HippoItem<E> other) {
        return relax(i -> i.isSame(other.get()), get(), RuntimeRepositoryException::new);
    }

    @Override
    public Item getAncestor(int depth) {
        return relax(i -> i.getAncestor(depth), get(), RuntimeRepositoryException::new);
    }

}
