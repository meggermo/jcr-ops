package nl.meg.jcr.predicate;


import nl.meg.jcr.HippoProperty;

import javax.jcr.Value;
import java.util.function.Predicate;

public interface PropertyPredicates {

    Predicate<HippoProperty> nameIn(String... names);

    Predicate<HippoProperty> pathIn(String... paths);

    Predicate<HippoProperty> with(Predicate<Value> valuePredicate);

    Predicate<HippoProperty> with(String name, Predicate<Value> valuePredicate);
}
