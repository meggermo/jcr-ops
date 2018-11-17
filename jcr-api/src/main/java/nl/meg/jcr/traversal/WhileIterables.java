package nl.meg.jcr.traversal;


import java.util.function.Predicate;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface WhileIterables {

    <X> Iterable<X> takeWhile(Predicate<X> p, Iterable<X> iterable);

    <X> Iterable<X> dropWhile(Predicate<X> p, Iterable<X> iterable);
}
