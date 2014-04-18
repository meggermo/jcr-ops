package nl.meg.jcr;

import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.Property;
import javax.jcr.Value;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static nl.meg.function.FunctionAdapter.relax;

public interface HippoProperty extends HippoItem<Property> {

    default boolean isMultiple() {
        return relax(Property::isMultiple, get(), RuntimeRepositoryException::new);
    }

    default Optional<Value> getValue() {
        return Optional.ofNullable(relax(p -> p.isMultiple() ? null : p.getValue(), get(), RuntimeRepositoryException::new));
    }

    default List<Value> getValues() {
        return relax(p -> p.isMultiple()
                        ? Stream.of(p.getValues()).collect(Collectors.toList())
                        : emptyList(),
                get(), RuntimeRepositoryException::new
        );
    }

}
