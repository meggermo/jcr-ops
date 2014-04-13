package nl.meg.jcr.internal;

import nl.meg.jcr.HippoItem;
import nl.meg.jcr.HippoNode;

import javax.jcr.Item;
import javax.jcr.Node;
import java.util.function.Function;

abstract class AbstractHippoItem<E extends Item> implements HippoItem<E> {

    private final E node;
    private final Function<Node, HippoNode> hippoNodeFactory;

    AbstractHippoItem(E node, Function<Node, HippoNode> hippoNodeFactory) {
        this.node = node;
        this.hippoNodeFactory = hippoNodeFactory;
    }

    @Override
    public final E get() {
        return node;
    }

    @Override
    public final HippoNode apply(Node node) {
        return hippoNodeFactory.apply(node);
    }
}
