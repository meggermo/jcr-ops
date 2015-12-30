package nl.meg.cr.internal;

import nl.meg.cr.Node;

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
        return JcrSupport.N.NODES.apply(delegate)
                .map(NodeImpl::new);
    }

    @Override
    public <T> Optional<T> getValue(String propertyName, Class<T> type) {
        return JcrSupport.N.single(propertyName, JcrSupport.V.get(type))
                .apply(delegate);
    }

    @Override
    public <T> Optional<List<T>> getValues(String propertyName, Class<T> type) {
        return JcrSupport.N.multi(propertyName, JcrSupport.V.get(type))
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
