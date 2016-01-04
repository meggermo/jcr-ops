package nl.meg.cr.support;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.Value;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Spliterator.*;
import static java.util.Spliterators.spliterator;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public final class NodeSupport {

    public static <T> Function<Node, Optional<T>> getValue(String propertyName, Function<Value, T> valueFn) {
        return property(propertyName)
                .andThen(p -> p.flatMap(getValue(valueFn)));
    }

    public static BiFunction<Node, String, Property> setValue(Value value) {
        return JcrSupport.wrap((node, propertyName) -> node.setProperty(propertyName, value));
    }

    public static BiFunction<String, Value, Property> setValue(Node node) {
        return JcrSupport.wrap((propertyName, value) -> node.setProperty(propertyName, value));
    }

    public static BiFunction<Node, Value, Property> setValue(String propertyName) {
        return JcrSupport.wrap((node, value) -> node.setProperty(propertyName, value));
    }

    public static <T> Function<Node, Optional<List<T>>> getValues(String propertyName, Function<Value, T> valueFn) {
        return property(propertyName)
                .andThen(p -> p.flatMap(getValues(valueFn)));
    }

    public static BiFunction<Node, String, Property> setValues(List<Value> values) {
        return JcrSupport.wrap((node, propertyName) -> node.setProperty(propertyName, values.toArray(new Value[values.size()])));
    }

    public static BiFunction<String, List<Value>, Property> setValues(Node node) {
        return JcrSupport.wrap((propertyName, values) -> node.setProperty(propertyName, values.toArray(new Value[values.size()])));
    }

    public static BiFunction<Node, List<Value>, Property> setValues(String propertyName) {
        return JcrSupport.wrap((node, values) -> node.setProperty(propertyName, values.toArray(new Value[values.size()])));
    }

    public static Function<Node, Stream<Node>> nodes() {
        return JcrSupport.wrap((Node n) -> n.getNodes())
                .andThen(NodeSupport::asStream);
    }

    private static Function<Node, Optional<Property>> property(String propertyName) {
        return JcrSupport.wrapOptional(node -> node.getProperty(propertyName));
    }

    private static <T> Function<Property, Optional<T>> getValue(Function<Value, T> valueFn) {
        return JcrSupport.wrapOptional(Property::getValue)
                .andThen(v -> v.map(valueFn));
    }

    private static <T> Function<Property, Optional<List<T>>> getValues(Function<Value, T> valueFn) {
        return property -> JcrSupport.wrapOptional(Property::getValues)
                .apply(property)
                .map(Stream::of)
                .map(valueStream -> valueStream.map(valueFn).collect(toList()));
    }

    @SuppressWarnings("unchecked")
    private static Stream<Node> asStream(NodeIterator iterator) {
        final int CHARACTERISTICS = SIZED | CONCURRENT | NONNULL;
        return stream(spliterator(iterator, iterator.getSize(), CHARACTERISTICS), false);
    }
}
