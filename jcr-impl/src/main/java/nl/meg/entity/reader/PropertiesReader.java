package nl.meg.entity.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

import nl.meg.jcr.entity.PropertyEntity;

public class PropertiesReader implements Function<Node, List<PropertyEntity<?>>> {

    private final Predicate<Property> filter;
    private final Function<Property, PropertyEntity<?>> reader;

    public PropertiesReader(Predicate<Property> filter, Function<Property, PropertyEntity<?>> reader) {
        Objects.requireNonNull(filter);
        Objects.requireNonNull(reader);
        this.filter = filter;
        this.reader = reader;
    }

    @Override
    public List<PropertyEntity<?>> apply(final Node node) {
        try {
            final List<PropertyEntity<?>> propertyEntities = new ArrayList<>();
            for (PropertyIterator iterator = node.getProperties(); iterator.hasNext(); ) {
                final Property p = iterator.nextProperty();
                if (filter.test(p)) {
                    propertyEntities.add(reader.apply(p));
                }
            }
            return propertyEntities;
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}
