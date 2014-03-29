package nl.meg.jcr.predicate;

import com.google.common.base.Predicate;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.nodetype.NodeType;

public interface NodePredicates {

    Predicate<Node> isSame(Node other);

    Predicate<Node> isNodeType(String nodeTypeName);

    Predicate<Node> identifierIn(String... identifiers);

    Predicate<Node> nameIn(String... names);

    Predicate<Node> withProperty(Predicate<Property> predicate);

    Predicate<Node> withNodeType(Predicate<NodeType> predicate);
}
