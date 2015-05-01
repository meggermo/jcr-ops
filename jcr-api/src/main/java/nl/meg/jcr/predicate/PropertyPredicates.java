package nl.meg.jcr.predicate;


import aQute.bnd.annotation.ProviderType;
import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.HippoValue;

import java.util.function.Predicate;

@ProviderType
public interface PropertyPredicates {

    Predicate<HippoProperty> nameIn(String... names);

    Predicate<HippoProperty> pathIn(String... paths);

    Predicate<HippoProperty> with(Predicate<HippoValue> valuePredicate);

    Predicate<HippoProperty> with(String name, Predicate<HippoValue> valuePredicate);
}
