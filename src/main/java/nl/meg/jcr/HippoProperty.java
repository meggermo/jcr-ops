package nl.meg.jcr;

import javax.jcr.Property;
import javax.jcr.Value;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static nl.meg.jcr.RepoFunctionInvoker.invoke;

public interface HippoProperty extends HippoItem<Property> {

    default boolean isMultiple() {
        return invoke(Property::isMultiple, get());
    }

    default Optional<Value> getValue() {
        return Optional.ofNullable(invoke(p -> p.isMultiple() ? null : p.getValue(), get()));
    }

    default List<Value> getValues() {
        return invoke(p -> p.isMultiple()
                        ? Stream.of(p.getValues()).collect(Collectors.toList())
                        : emptyList(),
                get()
        );
    }

}
