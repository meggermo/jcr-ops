package nl.meg.jcr.function;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import nl.meg.jcr.INode;

import javax.jcr.Property;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.util.Iterator;

public interface NodeFunctions {

    Function<INode, Session> getSession();

    Function<INode, String> getName();

    Function<INode, String> getPath();

    Function<INode, String> getIdentifier();

    Function<INode, NodeType> getPrimaryNodeType();

    Function<INode, Integer> getIndex();

    Function<INode, Optional<INode>> getParent();

    Function<INode, Iterator<INode>> getNodes();

    Function<INode, Optional<Property>> getProperty(String name);

    Function<INode, Iterator<Property>> getProperties();

    Function<INode, Iterator<NodeType>> getMixinNodeTypes();

}
