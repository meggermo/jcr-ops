package nl.meg.function;

public interface EFunction<T, R,E extends Exception> {

    R apply(T input) throws E;

}
