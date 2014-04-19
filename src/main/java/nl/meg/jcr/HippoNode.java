package nl.meg.jcr;


import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.Node;
import javax.jcr.nodetype.NodeType;
import java.util.List;
import java.util.Optional;

import static nl.meg.function.FunctionAdapter.relax;

public interface HippoNode extends HippoItem<Node> {

    default Integer getIndex() {
        return relax(Node::getIndex, get(), RuntimeRepositoryException::new);
    }

    default String getIdentifier() {
        return relax(Node::getIdentifier, get(), RuntimeRepositoryException::new);
    }

    default boolean isRoot() {
        return relax(n -> n.getSession().getRootNode().isSame(get()), get(), RuntimeRepositoryException::new);
    }

    default boolean isNodeType(String nodeTypeName) {
        return relax(n -> n.isNodeType(nodeTypeName), get(), RuntimeRepositoryException::new);
    }

    default NodeType getPrimaryNodeType() {
        return relax(Node::getPrimaryNodeType, get(), RuntimeRepositoryException::new);
    }

    default NodeType[] getMixinNodeTypes() {
        return relax(Node::getMixinNodeTypes, get(), RuntimeRepositoryException::new);
    }

    Optional<HippoNode> getNode(String name);

    List<HippoNode> getNodes();

    Optional<HippoProperty> getProperty(String name);

    List<HippoProperty> getProperties();
}
