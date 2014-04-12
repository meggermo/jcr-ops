package nl.meg.jcr.predicate.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.predicate.NodePredicates;

import javax.jcr.Property;
import javax.jcr.nodetype.NodeType;
import java.util.function.Predicate;

import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

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
        return n -> of(identifiers).anyMatch(i -> i.equals(n.getIdentifier()));
    }

    @Override
    public Predicate<HippoNode> nameIn(String... names) {
        return n -> of(names).anyMatch(i -> i.equals(n.getName()));
    }

    @Override
    public Predicate<HippoNode> withProperty(final Predicate<Property> predicate) {
        return n -> n.getProperties().stream().anyMatch(predicate);
    }

    @Override
    public Predicate<HippoNode> withNodeType(final Predicate<NodeType> predicate) {
        return node ->
                concat(of(node.getMixinNodeTypes()),
                        of(node.getPrimaryNodeType())).anyMatch(predicate);
    }

}
