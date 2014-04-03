package nl.meg.jcr;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.util.Iterator;

public interface INode extends Supplier<Node> {

    Integer getIndex();

    String getIdentifier();

    String getName();

    String getPath();

    Session getSession();

    boolean isRoot();

    boolean isSame(INode node);

    Optional<INode> getNode(String name);

    Iterator<INode> getNodes();

    Optional<Property> getProperty(String name);

    Iterator<Property> getProperties();

    boolean isNodeType(String nodeTypeName);

    NodeType getPrimaryNodeType();

    NodeType[] getMixinNodeTypes();

    Optional<INode> getParent();

}
