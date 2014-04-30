package nl.meg.jcr;


import javax.jcr.Node;
import javax.jcr.nodetype.NodeType;
import java.util.Optional;
import java.util.stream.Stream;

public interface HippoNode extends HippoItem<Node> {

    Integer getIndex();

    String getIdentifier();

    boolean isRoot();

    boolean isNodeType(String nodeTypeName);

    NodeType getPrimaryNodeType();

    NodeType[] getMixinNodeTypes();

    Optional<HippoNode> getNode(String name);

    Stream<HippoNode> getNodesAsStream();

    Optional<HippoProperty> getProperty(String name);

    Stream<HippoProperty> getPropertiesAsStream();
}
