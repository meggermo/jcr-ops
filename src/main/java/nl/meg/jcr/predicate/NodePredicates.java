package nl.meg.jcr.predicate;

import nl.meg.jcr.INode;

import javax.jcr.Property;
import javax.jcr.nodetype.NodeType;
import java.util.function.Predicate;

public interface NodePredicates {

    Predicate<INode> isSame(INode other);

    Predicate<INode> isNodeType(String nodeTypeName);

    Predicate<INode> identifierIn(String... identifiers);

    Predicate<INode> nameIn(String... names);

    Predicate<INode> withProperty(Predicate<Property> predicate);

    Predicate<INode> withNodeType(Predicate<NodeType> predicate);
}
