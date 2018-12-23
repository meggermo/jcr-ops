package nl.meg.entity.reader;

import java.util.List;
import java.util.Objects;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import nl.meg.jcr.entity.ImmutableNodeEntity;
import nl.meg.jcr.entity.NodeEntity;
import nl.meg.jcr.entity.PropertyEntity;
import nl.meg.jcr.function.JcrFunction;

public class NodeReader implements JcrFunction<Node, NodeEntity> {

    private final JcrFunction<Node, List<String>> typesReader;
    private final JcrFunction<Node, List<PropertyEntity<?>>> propertiesReader;
    private JcrFunction<Node, List<NodeEntity>> nodesReader;

    public NodeReader(JcrFunction<Node, List<String>> typesReader, JcrFunction<Node, List<PropertyEntity<?>>> propertiesReader) {
        Objects.requireNonNull(typesReader);
        Objects.requireNonNull(propertiesReader);
        this.typesReader = typesReader;
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
                .types(typesReader.apply(node))
                .properties(propertiesReader.apply(node))
                .nodes(nodesReader.apply(node))
                .build();
    }
}
