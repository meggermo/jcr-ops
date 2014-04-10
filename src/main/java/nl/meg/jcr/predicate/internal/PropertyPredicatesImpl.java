package nl.meg.jcr.predicate.internal;

import nl.meg.jcr.predicate.PropertyPredicates;

import javax.jcr.Property;
import javax.jcr.Value;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static nl.meg.jcr.RepoFunctionInvoker.invoke;

final class PropertyPredicatesImpl implements PropertyPredicates {

    @Override
    public Predicate<Property> nameIn(String... names) {
        return property -> Stream.of(names).anyMatch(name -> invoke(property, Property::getName).equals(name));
    }

    @Override
    public Predicate<Property> pathIn(String... paths) {
        return property -> Stream.of(paths).anyMatch(path -> invoke(property, Property::getPath).equals(path));
    }

    @Override
    public Predicate<Property> with(Predicate<Value> valuePredicate) {
        return property -> valuePredicate.test(invoke(property, Property::getValue));
    }

    @Override
    public Predicate<Property> with(String name, Predicate<Value> valuePredicate) {
        return property -> nameIn(name).test(property) && with(valuePredicate).test(property);
    }
}
