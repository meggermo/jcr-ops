package nl.meg.jcr.predicate.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.predicate.NodePredicates;

import javax.jcr.Property;
import javax.jcr.nodetype.NodeType;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.collect.Iterators.*;

final class NodePredicatesImpl implements NodePredicates {

    @Override
    public Predicate<HippoNode> isSame(HippoNode other) {
        return n -> n.isSame(other);
    }

    @Override
    public Predicate<HippoNode> isNodeType(final String nodeTypeName) {
        return n -> n.isNodeType(nodeTypeName);
    }

    @Override
    public Predicate<HippoNode> identifierIn(String... identifiers) {
        return n -> Stream.of(identifiers).anyMatch(i -> i.equals(n.getIdentifier()));
    }

    @Override
    public Predicate<HippoNode> nameIn(String... names) {
        return n -> Stream.of(names).anyMatch(i -> i.equals(n.getName()));
    }

    @Override
    public Predicate<HippoNode> withProperty(final Predicate<Property> predicate) {
        return n -> tryFind(n.getProperties(), predicate::test).isPresent();
    }

    @Override
    public Predicate<HippoNode> withNodeType(final Predicate<NodeType> predicate) {
        return node -> {
            final Iterator<NodeType> nodeTypeIterator = concat(
                    forArray(node.getMixinNodeTypes()),
                    singletonIterator(node.getPrimaryNodeType()));
            return tryFind(nodeTypeIterator, predicate::test).isPresent();
        };
    }

}
