package nl.meg.cr.internal;

import nl.meg.cr.Node;
import nl.meg.cr.support.NodeSupport;
import nl.meg.cr.support.ValueSupport;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

final class NodeImpl implements Node {

    private final javax.jcr.Node delegate;
    private final NodeSupport nodeSupport;
    private final ValueSupport valueSupport;

    NodeImpl(javax.jcr.Node node, NodeSupport nodeSupport, ValueSupport valueSupport) {
        this.delegate = node;
        this.nodeSupport = nodeSupport;
        this.valueSupport = valueSupport;
    }

    @Override
    public Stream<Node> getNodes() {
        return nodeSupport.nodes().apply(delegate)
                .map(n -> new NodeImpl(n, nodeSupport, valueSupport));
    }

    @Override
    public <T> Optional<T> getValue(String propertyName, Class<T> type) {
        return nodeSupport.single(propertyName, valueSupport.get(type))
                .apply(delegate);
    }

    @Override
    public <T> Optional<List<T>> getValues(String propertyName, Class<T> type) {
        return nodeSupport.multi(propertyName, valueSupport.get(type))
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
