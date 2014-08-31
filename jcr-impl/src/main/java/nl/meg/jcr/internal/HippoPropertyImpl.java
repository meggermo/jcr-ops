package nl.meg.jcr.internal;

import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.HippoValue;

import javax.jcr.Property;
import javax.jcr.Value;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

final class HippoPropertyImpl extends AbstractHippoItem<Property> implements HippoProperty {

    HippoPropertyImpl(Property property) {
        super(property);
    }

    @Override
    public boolean isMultiple() {
        return invoke(Property::isMultiple);
    }

    @Override
    public Optional<HippoValue> getValue() {
        return Optional.ofNullable(
                invoke(p -> p.isMultiple()
                        ? null
                        : value(p.getValue()))
        );
    }

    @Override
    public Stream<HippoValue> getValues() {
        return invoke(p -> p.isMultiple()
                ? Arrays.stream(p.getValues())
                : Stream.<Value>empty())
                .map(this::value);
    }

}
