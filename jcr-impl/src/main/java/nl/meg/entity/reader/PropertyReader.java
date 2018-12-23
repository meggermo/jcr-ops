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

    private static final PropertyEntityType[] MAP;

    static {
        MAP = new PropertyEntityType[PropertyType.DECIMAL + 1];

        MAP[PropertyType.BOOLEAN] = PropertyEntityType.BOOLEAN;
        MAP[PropertyType.DATE] = PropertyEntityType.DATE;
        MAP[PropertyType.DECIMAL] = PropertyEntityType.BIGDECIMAL;
        MAP[PropertyType.DOUBLE] = PropertyEntityType.DOUBLE;
        MAP[PropertyType.LONG] = PropertyEntityType.LONG;
        MAP[PropertyType.PATH] = PropertyEntityType.PATH;
        MAP[PropertyType.URI] = PropertyEntityType.URI;

        MAP[PropertyType.STRING] = PropertyEntityType.STRING;
        MAP[PropertyType.NAME] = PropertyEntityType.STRING;
        MAP[PropertyType.REFERENCE] = PropertyEntityType.STRING;
        MAP[PropertyType.WEAKREFERENCE] = PropertyEntityType.STRING;

        MAP[PropertyType.BINARY] = PropertyEntityType.UNSUPPORTED;
        MAP[PropertyType.UNDEFINED] = PropertyEntityType.UNSUPPORTED;
    }

    private final Function<Property, UnaryOperator<String>> stringPostProcessor;

    public PropertyReader(Function<Property, UnaryOperator<String>> stringPostProcessor) {
        Objects.requireNonNull(stringPostProcessor);
        this.stringPostProcessor = stringPostProcessor;
    }

    @Override
    public PropertyEntity<?> apply(final Property property) {
        try {

            final PropertyEntityType type = MAP[property.getType()];
            return ImmutablePropertyEntity.builder()
                    .name(property.getName())
                    .multiple(property.isMultiple())
                    .type(type)
                    .values(values(type, property))
                    .build();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private List<?> values(PropertyEntityType type, Property property) throws RepositoryException {
        switch (type) {

            case BIGDECIMAL:
                return convert(values(property), Value::getDecimal);

            case BOOLEAN:
                return convert(values(property), Value::getBoolean);

            case DATE:
                return convert(values(property), Value::getDate);

            case DOUBLE:
                return convert(values(property), Value::getDouble);

            case LONG:
                return convert(values(property), Value::getLong);

            case PATH:
            case STRING:
            case URI:
                return convert(values(property), v -> stringPostProcessor.apply(property).apply(v.getString()));

            default:
                throw new IllegalArgumentException(PropertyType.nameFromValue(property.getType()));
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
