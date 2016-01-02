package nl.meg.cr.support;

import nl.meg.cr.support.JcrSupport;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.Value;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Spliterator.*;
import static java.util.Spliterators.spliterator;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public final class NodeSupport {

    private final Function<Node, Stream<Node>> nodes;
    private final JcrSupport jcrSupport;

    public <T> Function<Node, Optional<T>> single(String propertyName, Function<Value, T> valueFn) {
        return property(propertyName)
                .andThen(p -> p.flatMap(single(valueFn)));
    }

    public <T> Function<Node, Optional<List<T>>> multi(String propertyName, Function<Value, T> valueFn) {
        return property(propertyName)
                .andThen(p -> p.flatMap(multi(valueFn)));
    }

    public Function<Node, Stream<Node>> nodes() {
        return nodes;
    }

    public NodeSupport(JcrSupport jcrSupport) {
        this.jcrSupport = jcrSupport;
        this.nodes = jcrSupport.wrap((Node n) -> n.getNodes())
                .andThen(this::asStream);
    }

    private Function<Node, Optional<Property>> property(String propertyName) {
        return jcrSupport.wrapOptional((Node node) -> node.getProperty(propertyName));
    }

    private <T> Function<Property, Optional<T>> single(Function<Value, T> valueFn) {
        return jcrSupport.wrapOptional(Property::getValue)
                .andThen(v -> v.map(valueFn));
    }

    private <T> Function<Property, Optional<List<T>>> multi(Function<Value, T> valueFn) {
        return property -> jcrSupport.wrapOptional(Property::getValues)
                .apply(property)
                .map(Stream::of)
                .map(valueStream -> valueStream.map(valueFn).collect(toList()));
    }

    @SuppressWarnings("unchecked")
    private Stream<Node> asStream(NodeIterator iterator) {
        final int CHARACTERISTICS = SIZED | CONCURRENT | NONNULL;
        return stream(spliterator(iterator, iterator.getSize(), CHARACTERISTICS), false);
    }
}
