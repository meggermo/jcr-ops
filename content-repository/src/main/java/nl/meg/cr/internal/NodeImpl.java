package nl.meg.cr.internal;

import nl.meg.cr.Node;
import nl.meg.cr.Property;
import nl.meg.cr.RepositoryException;
import nl.meg.cr.internal.ExceptionSupport.EFunction;

import javax.jcr.NodeIterator;
import javax.jcr.PropertyIterator;
import java.util.stream.Stream;

import static nl.meg.cr.internal.ExceptionSupport.tryInvoke;
import static nl.meg.cr.internal.RangeIteratorSupport.stream;

final class NodeImpl implements Node {

    private final javax.jcr.Node delegate;

    NodeImpl(javax.jcr.Node node) {
        this.delegate = node;
    }

    @Override
    public Stream<Node> getNodes() {
        return stream(tryGet(javax.jcr.Node::getNodes, NodeIterator.class))
                .map(NodeImpl::new);
    }

    @Override
    public Stream<Property> getProperties() {
        return stream(tryGet(javax.jcr.Node::getProperties, PropertyIterator.class))
                .map(PropertyImpl::new);
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

    private <Y> Y tryGet(EFunction<javax.jcr.Node, Y, javax.jcr.RepositoryException> f, Class<Y> type) {
        return tryInvoke(f, delegate, RepositoryException::new);
    }

}
