package nl.meg.jcr.predicate;

import aQute.bnd.annotation.ProviderType;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoProperty;

import javax.jcr.nodetype.NodeType;
import java.util.function.Predicate;

@ProviderType
public interface NodePredicates {

    Predicate<HippoNode> isSame(HippoNode other);

    Predicate<HippoNode> isNodeType(String nodeTypeName);

    Predicate<HippoNode> identifierIn(String... identifiers);

    Predicate<HippoNode> nameIn(String... names);

    Predicate<HippoNode> withProperty(Predicate<HippoProperty> predicate);

    Predicate<HippoNode> withNodeType(Predicate<NodeType> predicate);
}
