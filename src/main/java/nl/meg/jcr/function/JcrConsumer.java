package nl.meg.jcr.function;

import javax.jcr.RepositoryException;

@FunctionalInterface
public interface JcrConsumer<T> {

    void accept(T value) throws RepositoryException;

    default <U> JcrBiFunction<T, U, U> consumeFirst() {
        return (t, u) -> {
            accept(t);
            return u;
        };
    }
}
