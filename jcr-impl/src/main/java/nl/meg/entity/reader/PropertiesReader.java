package nl.meg.entity.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

import nl.meg.jcr.entity.PropertyEntity;
import nl.meg.jcr.function.JcrFunction;

public class PropertiesReader implements JcrFunction<Node, List<PropertyEntity<?>>> {

    private final Predicate<Property> filter;
    private final JcrFunction<Property, PropertyEntity<?>> reader;

    public PropertiesReader(Predicate<Property> filter, JcrFunction<Property, PropertyEntity<?>> reader) {
        Objects.requireNonNull(filter);
        Objects.requireNonNull(reader);
        this.filter = filter;
        this.reader = reader;
    }

    @Override
    public List<PropertyEntity<?>> apply(final Node node) throws RepositoryException {
        final List<PropertyEntity<?>> propertyEntities = new ArrayList<>();
        for (PropertyIterator iterator = node.getProperties(); iterator.hasNext(); ) {
            final Property p = iterator.nextProperty();
            if (filter.test(p)) {
                propertyEntities.add(reader.apply(p));
            }
        }
        return propertyEntities;
    }
}
