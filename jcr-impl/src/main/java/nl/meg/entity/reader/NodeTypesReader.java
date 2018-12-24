package nl.meg.entity.reader;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.jcr.nodetype.NodeType;

public class NodeTypesReader implements Function<NodeType, List<String>> {

    private final Predicate<NodeType> filter;

    public NodeTypesReader(Predicate<NodeType> filter) {
        Objects.requireNonNull(filter);
        this.filter = filter;
    }

    @Override
    public List<String> apply(final NodeType nodeType) {
        return flatten(nodeType)
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
