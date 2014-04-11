package nl.meg.jcr.predicate;

import nl.meg.jcr.HippoNode;

import javax.jcr.Property;
import javax.jcr.nodetype.NodeType;
import java.util.function.Predicate;

public interface NodePredicates {

    Predicate<HippoNode> isSame(HippoNode other);

    Predicate<HippoNode> isNodeType(String nodeTypeName);

    Predicate<HippoNode> identifierIn(String... identifiers);

    Predicate<HippoNode> nameIn(String... names);

    Predicate<HippoNode> withProperty(Predicate<Property> predicate);

    Predicate<HippoNode> withNodeType(Predicate<NodeType> predicate);
}
