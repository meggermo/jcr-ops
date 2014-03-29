package nl.meg.jcr.function.internal;

import com.google.common.base.Function;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import nl.meg.jcr.function.ValueFunctions;

import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.Calendar;

final class ValueFunctionsImpl implements ValueFunctions {

    @Override
    public Function<Value, String> getString() {
        return GET_STRING;
    }

    @Override
    public Function<Value, Boolean> getBoolean() {
        return GET_BOOLEAN;
    }

    @Override
    public Function<Value, Long> getLong() {
        return GET_LONG;
    }

    @Override
    public Function<Value, Calendar> getDate() {
        return GET_DATE;
    }

    private static final Function<Value, String> GET_STRING = new Function<Value, String>() {
        @Override
        public String apply(Value value) {
            try {
                return value.getString();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static final Function<Value, Boolean> GET_BOOLEAN = new Function<Value, Boolean>() {
        @Override
        public Boolean apply(Value value) {
            try {
                return value.getBoolean();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static final Function<Value, Long> GET_LONG = new Function<Value, Long>() {
        @Override
        public Long apply(Value value) {
            try {
                return value.getLong();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static final Function<Value, Calendar> GET_DATE = new Function<Value, Calendar>() {
        @Override
        public Calendar apply(Value value) {
            try {
                return value.getDate();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

}
