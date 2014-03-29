package nl.meg.jcr.predicate.internal;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import nl.meg.jcr.function.PropertyFunctions;
import nl.meg.jcr.predicate.PropertyPredicates;

import javax.jcr.Property;
import javax.jcr.Value;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.in;

final class PropertyPredicatesImpl implements PropertyPredicates {

    private final PropertyFunctions propertyFunctions;

    PropertyPredicatesImpl(PropertyFunctions propertyFunctions) {
        this.propertyFunctions = propertyFunctions;
    }

    @Override
    public Predicate<Property> nameIn(String... names) {
        final ImmutableSet<String> strings = ImmutableSet.<String>builder().add(names).build();
        return compose(in(strings), propertyFunctions.getName());
    }

    @Override
    public Predicate<Property> pathIn(String... paths) {
        final ImmutableSet<String> strings = ImmutableSet.<String>builder().add(paths).build();
        return compose(in(strings), propertyFunctions.getPath());
    }

    @Override
    public Predicate<Property> with(Predicate<Value> valuePredicate) {
        return compose(valuePredicate, propertyFunctions.getValue());
    }

    @Override
    public Predicate<Property> with(String name, Predicate<Value> valuePredicate) {
        return Predicates.and(nameIn(name), with(valuePredicate));
    }
}
