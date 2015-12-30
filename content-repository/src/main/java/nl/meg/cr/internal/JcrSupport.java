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

    static final class V {

        private static final Map<Class<?>, Function<Value, ?>> VALUE_MAP;

        static {
            VALUE_MAP = new HashMap<>();
            VALUE_MAP.put(String.class, JcrSupport.wrap(Value::getString));
        }

        @SuppressWarnings("unchecked")
        public static <T> Function<Value, T> get(Class<T> type) {
            return (Function<Value, T>) VALUE_MAP.get(type);
        }

    }

    static final class N {

        static Function<Node, Stream<Node>> NODES =
                wrap((Node n) -> n.getNodes()).andThen(RangeIteratorSupport::stream);

        static <T> Function<Node, Optional<T>> single(String propertyName, Function<Value, T> valueFn) {
            return property(propertyName)
                    .andThen(p -> p.flatMap(JcrSupport.P.single(valueFn)));
        }

        static <T> Function<Node, Optional<List<T>>> multi(String propertyName, Function<Value, T> valueFn) {
            return property(propertyName)
                    .andThen(p -> p.flatMap(JcrSupport.P.multi(valueFn)));
        }

        private static Function<Node, Optional<Property>> property(String propertyName) {
            return wrapOptional((Node node) -> node.getProperty(propertyName));
        }
    }

    static final class P {
        private static <T> Function<Property, Optional<T>> single(Function<Value, T> valueFn) {
            return wrapOptional(Property::getValue)
                    .andThen(v -> v.map(valueFn));
        }

        private static <T> Function<Property, Optional<List<T>>> multi(Function<Value, T> valueFn) {
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
