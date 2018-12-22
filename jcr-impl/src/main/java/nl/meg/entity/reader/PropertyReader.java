package nl.meg.entity.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import nl.meg.jcr.entity.ImmutablePropertyEntity;
import nl.meg.jcr.entity.PropertyEntity;
import nl.meg.jcr.entity.PropertyEntityType;
import nl.meg.jcr.function.JcrFunction;

public class PropertyReader implements Function<Property, PropertyEntity<?>> {

    private final Function<Property, UnaryOperator<String>> stringPostProcessor;

    public PropertyReader(Function<Property, UnaryOperator<String>> stringPostProcessor) {
        Objects.requireNonNull(stringPostProcessor);
        this.stringPostProcessor = stringPostProcessor;
    }

    @Override
    public PropertyEntity<?> apply(final Property property) {
        try {
            final ImmutablePropertyEntity.Builder<Object> builder = ImmutablePropertyEntity.builder()
                    .name(property.getName())
                    .multiple(property.isMultiple());
            final Value[] values = values(property);
            switch (property.getType()) {
                case PropertyType.DECIMAL:
                    builder.type(PropertyEntityType.BIGDECIMAL);
                    return builder.values(convert(values, Value::getDecimal)).build();
                case PropertyType.BOOLEAN:
                    builder.type(PropertyEntityType.BOOLEAN);
                    return builder.values(convert(values, Value::getBoolean)).build();
                case PropertyType.DATE:
                    builder.type(PropertyEntityType.DATE);
                    return builder.values(convert(values, v -> v.getDate().toInstant())).build();
                case PropertyType.DOUBLE:
                    builder.type(PropertyEntityType.DOUBLE);
                    return builder.values(convert(values, Value::getDouble)).build();
                case PropertyType.LONG:
                    builder.type(PropertyEntityType.LONG);
                    return builder.values(convert(values, Value::getLong)).build();
                case PropertyType.NAME:
                case PropertyType.PATH:
                case PropertyType.REFERENCE:
                case PropertyType.STRING:
                case PropertyType.WEAKREFERENCE:
                    builder.type(PropertyEntityType.STRING);
                    return builder.values(convert(values, v -> stringPostProcessor.apply(property).apply(v.getString()))).build();
                default:
                    throw new IllegalArgumentException(PropertyType.nameFromValue(property.getType()));
            }
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private Value[] values(Property property) throws RepositoryException {
        return property.isMultiple() ? property.getValues() : new Value[]{property.getValue()};
    }

    private <T> List<?> convert(Value[] values, JcrFunction<Value, T> f) throws RepositoryException {
        final List<T> valueList = new ArrayList<>();
        for (Value value : values) {
            valueList.add(f.apply(value));
        }
        return valueList;
    }
}
