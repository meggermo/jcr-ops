package nl.meg.jcr.traversal;


import java.util.function.Predicate;

public interface WhileIterables {

    <X> Iterable<X> takeWhile(Predicate<X> p, Iterable<X> iterable);

    <X> Iterable<X> dropWhile(Predicate<X> p, Iterable<X> iterable);
}
