package nl.meg.jcr;


import javax.jcr.Node;
import javax.jcr.nodetype.NodeType;
import java.util.List;
import java.util.Optional;

public interface HippoNode extends HippoItem<Node> {

    Integer getIndex();

    String getIdentifier();

    boolean isRoot();

    boolean isNodeType(String nodeTypeName);

    NodeType getPrimaryNodeType();

    NodeType[] getMixinNodeTypes();

    Optional<HippoNode> getNode(String name);

    List<HippoNode> getNodes();

    Optional<HippoProperty> getProperty(String name);

    List<HippoProperty> getProperties();
}
