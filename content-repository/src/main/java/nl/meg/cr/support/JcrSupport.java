package nl.meg.cr.support;

import javax.jcr.ItemNotFoundException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

final class JcrSupport {

    interface JcrFn<T, R> {
        R apply(T t) throws RepositoryException;
    }

    static <T, R> Function<T, R> wrap(JcrFn<T, R> jcrFn) {
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

    static <T, R> Function<T, Optional<R>> wrapOptional(JcrFn<T, R> jcrFn) {
        return wrap(jcrFn).andThen(Optional::ofNullable);
    }

    interface JcrBiFn<S, T, R> {
        R apply(S s, T t) throws RepositoryException;
    }


    static <S, T, R> BiFunction<S, T, R> wrap(JcrBiFn<S, T, R> jcrFn) {
        return (s, t) -> {
            try {
                return jcrFn.apply(s, t);
            } catch (ItemNotFoundException | PathNotFoundException e) {
                return null;
            } catch (RepositoryException e) {
                throw new nl.meg.cr.RepositoryException(e);
            }
        };
    }

    static <S, T, R> BiFunction<S, T, Optional<R>> wrapOptional(JcrBiFn<S, T, R> jcrFn) {
        return wrap(jcrFn).andThen(Optional::ofNullable);
    }

    private JcrSupport() {
    }
}
