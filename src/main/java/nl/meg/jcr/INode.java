package nl.meg.jcr;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.util.Iterator;

public interface INode extends Supplier<Node> {

    String getName();

    String getPath();

    Session getSession();

    boolean isSame(INode node);

    NodeType getPrimaryNodeType();

    Optional<INode> getParent();

    Iterator<INode> getNodes();

    boolean hasNodes();

    boolean hasNode(String name);

    boolean isNodeType(String nodeTypeName);

    boolean hasProperty(String name);

    Property getProperty(String name);

    String getIdentifier();

    boolean hasProperties();

    Iterator<Property> getProperties();

    Integer getIndex();

    NodeType[] getMixinNodeTypes();

    boolean isRoot();
}
