package nl.meg.jcr.function.internal;

import com.google.common.base.Function;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import nl.meg.jcr.function.PropertyFunctions;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

final class PropertyFunctionsImpl implements PropertyFunctions {

    @Override
    public Function<Property, String> getName() {
        return GET_NAME;
    }

    @Override
    public Function<Property, String> getPath() {
        return GET_PATH;
    }

    @Override
    public Function<Property, Value> getValue() {
        return GET_VALUE;
    }

    private static Function<Property, String> GET_NAME = new Function<Property, String>() {
        @Override
        public String apply(Property property) {
            try {
                return property.getName();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static Function<Property, String> GET_PATH = new Function<Property, String>() {
        @Override
        public String apply(Property property) {
            try {
                return property.getPath();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static Function<Property, Value> GET_VALUE = new Function<Property, Value>() {
        @Override
        public Value apply(Property property) {
            try {
                return property.getValue();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };
}
