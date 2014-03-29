package nl.meg.jcr.function;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.nodetype.NodeType;
import java.util.Iterator;

public interface NodeFunctions {

    Function<Node, String> getName();

    Function<Node, String> getPath();

    Function<Node, String> getIdentifier();

    Function<Node, NodeType> getPrimaryNodeType();

    Function<Node, Integer> getIndex();

    Function<Node, Optional<Node>> getParent();

    Function<Node, Iterator<Node>> getNodes();

    Function<Node, Iterator<Property>> getProperties();

    Function<Node, Iterator<NodeType>> getMixinNodeTypes();

}
