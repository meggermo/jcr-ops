package nl.meg.entity.reader;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

public class TypesReader implements Function<Node, List<String>> {

    private final Predicate<NodeType> filter;

    public TypesReader(Predicate<NodeType> filter) {
        Objects.requireNonNull(filter);
        this.filter = filter;
    }

    @Override
    public List<String> apply(final Node node) {
        try {
            return flatten(node.getPrimaryNodeType())
                    .filter(filter)
                    .map(NodeType::getName)
                    .collect(Collectors.toList());
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<NodeType> flatten(NodeType nodeType) {
        return Stream.concat(
                Stream.of(nodeType),
                Stream.of(nodeType.getSupertypes()).flatMap(TypesReader::flatten)
        );
    }
}
