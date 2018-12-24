package nl.meg.entity.reader;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

import nl.meg.jcr.entity.ImmutableNodeEntity;
import nl.meg.jcr.entity.NodeEntity;
import nl.meg.jcr.entity.PropertyEntity;
import nl.meg.jcr.function.JcrFunction;

public class NodeReader implements JcrFunction<Node, NodeEntity> {

    private final Function<NodeType, List<String>> nodeTypesReader;
    private final JcrFunction<Node, List<PropertyEntity<?>>> propertiesReader;
    private JcrFunction<Node, List<NodeEntity>> nodesReader;

    public NodeReader(Function<NodeType, List<String>> nodeTypesReader, JcrFunction<Node, List<PropertyEntity<?>>> propertiesReader) {
        Objects.requireNonNull(nodeTypesReader);
        Objects.requireNonNull(propertiesReader);
        this.nodeTypesReader = nodeTypesReader;
        this.propertiesReader = propertiesReader;
    }

    public void setNodesReader(final JcrFunction<Node, List<NodeEntity>> nodesReader) {
        Objects.requireNonNull(nodesReader);
        this.nodesReader = nodesReader;
    }

    @Override
    public NodeEntity apply(final Node node) throws RepositoryException {
        return ImmutableNodeEntity.builder()
                .id(node.getIdentifier())
                .name(node.getName())
                .path(node.getPath())
                .types(nodeTypesReader.apply(node.getPrimaryNodeType()))
                .properties(propertiesReader.apply(node))
                .nodes(nodesReader.apply(node))
                .build();
    }
}
