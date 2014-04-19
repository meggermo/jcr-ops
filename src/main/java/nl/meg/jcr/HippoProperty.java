package nl.meg.jcr;

import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.Property;
import java.util.List;
import java.util.Optional;

import static nl.meg.function.FunctionAdapter.relax;

public interface HippoProperty extends HippoItem<Property> {

    default boolean isMultiple() {
        return relax(Property::isMultiple, get(), RuntimeRepositoryException::new);
    }

    Optional<HippoValue> getValue();

    List<HippoValue> getValues();

}
