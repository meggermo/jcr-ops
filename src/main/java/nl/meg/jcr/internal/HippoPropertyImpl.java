package nl.meg.jcr.internal;

import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.HippoValue;
import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.Property;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static nl.meg.function.FunctionAdapter.relax;

final class HippoPropertyImpl extends AbstractHippoItem<Property> implements HippoProperty {

    HippoPropertyImpl(Property property) {
        super(property);
    }

    @Override
    public boolean isMultiple() {
        return relax(Property::isMultiple, get(), RuntimeRepositoryException::new);
    }

    @Override
    public Optional<HippoValue> getValue() {
        return Optional.ofNullable(
                relax(p -> p.isMultiple()
                                ? null
                                : value(p.getValue()),
                        get(), RuntimeRepositoryException::new
                )
        );
    }

    @Override
    public List<HippoValue> getValues() {
        return relax(p -> p.isMultiple()
                        ? Stream.of(p.getValues()).map(HippoValueImpl::new).collect(Collectors.toList())
                        : emptyList(),
                get(), RuntimeRepositoryException::new
        );
    }

}
