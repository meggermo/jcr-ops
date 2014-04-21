package nl.meg.jcr.internal;

import nl.meg.function.EFunction;
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