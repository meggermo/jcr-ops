package nl.meg.jcr.predicate.internal;

import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.HippoValue;
import nl.meg.jcr.predicate.PropertyPredicates;

import java.util.function.Predicate;
import java.util.stream.Stream;

final class PropertyPredicatesImpl implements PropertyPredicates {

    @Override
    public Predicate<HippoProperty> nameIn(String... names) {
        return property -> Stream.of(names).anyMatch(name -> name == property.getName());
    }

    @Override
    public Predicate<HippoProperty> pathIn(String... paths) {
        return property -> Stream.of(paths).anyMatch(path -> path == property.getPath());
    }

    @Override
    public Predicate<HippoProperty> with(Predicate<HippoValue> valuePredicate) {
        return property -> valuePredicate.test(property.getValue().get());
    }

    @Override
    public Predicate<HippoProperty> with(String name, Predicate<HippoValue> valuePredicate) {
        return property -> nameIn(name).test(property) && with(valuePredicate).test(property);
    }
}
