package nl.meg.cr.internal;

import java.util.function.Function;

final class ExceptionSupport {

    public interface EFunction<X, Y, E extends Exception> {
        Y eval(X x) throws E;
    }

    @SuppressWarnings("unchecked")
    static <X, Y, E extends Exception, R extends RuntimeException>
    Y tryInvoke(EFunction<X, Y, E> f, X x, Function<E, R> g) {
        try {
            return f.eval(x);
        } catch (Exception e) {
            throw g.apply((E) e);
        }
    }
}
