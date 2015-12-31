package nl.meg.cr.internal;

import javax.jcr.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

final class JcrSupport {

    public static final V v = new V();
    public static final N n = new N();

    public static final class V {

        private final Map<Class<?>, Function<Value, ?>> valueFnMap;

        @SuppressWarnings("unchecked")
        public <T> Function<Value, T> get(Class<T> type) {
            return (Function<Value, T>) valueFnMap.get(type);
        }

        private V() {
            valueFnMap = new HashMap<>();
            valueFnMap.put(String.class, wrap(Value::getString));
        }
    }

    public static final class N {

        private final Function<Node, Stream<Node>> nodes;

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

        private N() {
            this.nodes = wrap((Node n) -> n.getNodes())
                    .andThen(RangeIteratorSupport::asStream);
        }

        private Function<Node, Optional<Property>> property(String propertyName) {
            return wrapOptional((Node node) -> node.getProperty(propertyName));
        }

        private <T> Function<Property, Optional<T>> single(Function<Value, T> valueFn) {
            return wrapOptional(Property::getValue)
                    .andThen(v -> v.map(valueFn));
        }

        private <T> Function<Property, Optional<List<T>>> multi(Function<Value, T> valueFn) {
            return property -> wrapOptional(Property::getValues)
                    .apply(property)
                    .map(Stream::of)
                    .map(valueStream -> valueStream.map(valueFn).collect(toList()));
        }
    }

    private interface JcrFn<T, R> {
        R apply(T t) throws RepositoryException;
    }

    private static <T, R> Function<T, R> wrap(JcrFn<T, R> jcrFn) {
        return t -> {
            try {
                return jcrFn.apply(t);
            } catch (ItemNotFoundException | PathNotFoundException e) {
                return null;
            } catch (RepositoryException e) {
                throw new nl.meg.cr.RepositoryException(e);
            }
        };
    }

    private static <T, R> Function<T, Optional<R>> wrapOptional(JcrFn<T, R> jcrFn) {
        return wrap(jcrFn).andThen(Optional::ofNullable);
    }
}
