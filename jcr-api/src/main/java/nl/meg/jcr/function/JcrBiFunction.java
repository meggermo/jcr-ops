package nl.meg.jcr.function;

import javax.jcr.RepositoryException;

@FunctionalInterface
public interface JcrBiFunction<T, U, R> {

    R apply(T t, U u) throws RepositoryException;

    default JcrFunction<U, R> bindFirst(T t) {
        return u -> apply(t, u);
    }

    default JcrFunction<T, R> bindSecond(U u) {
        return t -> apply(t, u);
    }

    default <V> JcrBiFunction<T, U, V> andThen(JcrFunction<? super R, ? extends V> after) {
        return (t, u) -> after.apply(apply(t, u));
    }

}
