package nl.meg.jcr.predicate;


import javax.jcr.Property;
import javax.jcr.Value;
import java.util.function.Predicate;

public interface PropertyPredicates {

    Predicate<Property> nameIn(String... names);

    Predicate<Property> pathIn(String... paths);

    Predicate<Property> with(Predicate<Value> valuePredicate);

    Predicate<Property> with(String name, Predicate<Value> valuePredicate);
}
