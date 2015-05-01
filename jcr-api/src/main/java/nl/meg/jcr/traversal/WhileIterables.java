package nl.meg.jcr.traversal;


import aQute.bnd.annotation.ProviderType;

import java.util.function.Predicate;

@ProviderType
public interface WhileIterables {

    <X> Iterable<X> takeWhile(Predicate<X> p, Iterable<X> iterable);

    <X> Iterable<X> dropWhile(Predicate<X> p, Iterable<X> iterable);
}
