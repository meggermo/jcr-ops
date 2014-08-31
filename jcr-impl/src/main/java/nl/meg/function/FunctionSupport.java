package nl.meg.function;

import java.util.function.Function;

public final class FunctionSupport {

    /**
     * Calls a function that throws a checked {@link Exception} of type E and returns the result if no exception occurs. If calling the function results in an exception
     * then the given function g is used to throw an unchecked exception instead.
     *
     * @param f     a function that throws a checked exception
     * @param input value for the function
     * @param g     transformation function E -> RE
     * @param <T>   type of input
     * @param <R>   type of output
     * @param <E>   type of checked exception
     * @param <RE>  type of unchecked exception
     * @return result of f applied to input
     */
    public static <T, R, E extends Exception, RE extends RuntimeException> R relax(EFunction<? super T, ? extends R, E> f, T input, Function<E, ? extends RE> g) {
        return FS.doRelax(f, input, g);
    }

    private static final FunctionSupport FS = new FunctionSupport();

    private FunctionSupport() {

    }

    @SuppressWarnings("unchecked")
    private <T, R, E extends Exception, RE extends RuntimeException> R doRelax(EFunction<? super T, ? extends R, E> f, T input, Function<E, ? extends RE> g) {
        try {
            return f.apply(input);
        } catch (Exception e) {
            // Safe cast, because f can only throw an instance of (a subclass of) E
            throw g.apply((E) e);
        }
    }
}
