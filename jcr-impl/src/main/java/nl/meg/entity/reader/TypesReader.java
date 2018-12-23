package nl.meg.entity.reader;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

import nl.meg.jcr.function.JcrFunction;

public class TypesReader implements JcrFunction<Node, List<String>> {

    private final Predicate<NodeType> filter;

    public TypesReader(Predicate<NodeType> filter) {
        Objects.requireNonNull(filter);
        this.filter = filter;
    }

    @Override
    public List<String> apply(final Node node) throws RepositoryException {
        return flatten(node.getPrimaryNodeType())
                .map(NodeType::getName)
                .collect(Collectors.toList());
    }

    private Stream<NodeType> flatten(NodeType nodeType) {
        return Stream.concat(
                Stream.of(nodeType)
                        .filter(filter),
                Stream.of(nodeType.getSupertypes())
                        .filter(filter)
                        .flatMap(this::flatten)
        );
    }
}
