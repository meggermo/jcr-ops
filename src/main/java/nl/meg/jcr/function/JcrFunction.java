package nl.meg.jcr.function;

import javax.jcr.RepositoryException;

@FunctionalInterface
public interface JcrFunction<T, R> {

    R apply(T t) throws RepositoryException;

    default <V> JcrFunction<V, R> compose(JcrFunction<? super V, ? extends T> before) {
        return v -> apply(before.apply(v));
    }

    default <V> JcrFunction<T, V> andThen(JcrFunction<? super R, ? extends V> after) {
        return t -> after.apply(apply(t));
    }

    static <T> JcrFunction<T, T> identity() {
        return t -> t;
    }
}
