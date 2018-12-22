package nl.meg.entity.reader;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import nl.meg.jcr.entity.ImmutableNodeEntity;
import nl.meg.jcr.entity.NodeEntity;
import nl.meg.jcr.entity.PropertyEntity;

public class NodeReader implements Function<Node, NodeEntity> {

    private final Function<Node, List<String>> typesReader;
    private final Function<Node, List<PropertyEntity<?>>> propertiesReader;
    private Function<Node, List<NodeEntity>> nodesReader;

    public NodeReader(Function<Node, List<String>> typesReader, Function<Node, List<PropertyEntity<?>>> propertiesReader) {
        Objects.requireNonNull(typesReader);
        Objects.requireNonNull(propertiesReader);
        this.typesReader = typesReader;
        this.propertiesReader = propertiesReader;
    }

    public void setNodesReader(final Function<Node, List<NodeEntity>> nodesReader) {
        Objects.requireNonNull(nodesReader);
        this.nodesReader = nodesReader;
    }

    @Override
    public NodeEntity apply(final Node node) {
        try {
            return ImmutableNodeEntity.builder()
                    .id(node.getIdentifier())
                    .name(node.getName())
                    .path(node.getPath())
                    .types(typesReader.apply(node))
                    .properties(propertiesReader.apply(node))
                    .nodes(nodesReader.apply(node))
                    .build();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}
