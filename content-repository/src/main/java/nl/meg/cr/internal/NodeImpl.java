package nl.meg.cr.internal;

import nl.meg.cr.Node;
import nl.meg.cr.support.NodeSupport;
import nl.meg.cr.support.ValueSupport;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

final class NodeImpl implements Node {

    private final javax.jcr.Node delegate;

    NodeImpl(javax.jcr.Node node) {
        this.delegate = node;
    }

    @Override
    public Stream<Node> getNodes() {
        return NodeSupport.nodes().apply(delegate)
                .map(n -> new NodeImpl(n));
    }

    @Override
    public <T> Optional<T> getValue(String propertyName, Class<T> type) {
        return NodeSupport.single(propertyName, ValueSupport.get(type))
                .apply(delegate);
    }

    @Override
    public <T> Optional<List<T>> getValues(String propertyName, Class<T> type) {
        return NodeSupport.multi(propertyName, ValueSupport.get(type))
                .apply(delegate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeImpl node = (NodeImpl) o;

        return delegate.equals(node.delegate);

    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

}
