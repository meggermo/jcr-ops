package nl.meg.jcr.predicate.internal;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import nl.meg.jcr.function.ValueFunctions;
import nl.meg.jcr.predicate.ValuePredicates;

import javax.jcr.Value;
import java.util.Calendar;

final class ValuePredicatesImpl implements ValuePredicates {

    private final ValueFunctions valueFunctions;

    public ValuePredicatesImpl(ValueFunctions valueFunctions) {
        this.valueFunctions = valueFunctions;
    }

    @Override
    public Predicate<Value> equalTo(String value) {
        return Predicates.compose(Predicates.equalTo(value), valueFunctions.getString());
    }

    @Override
    public Predicate<Value> equalTo(Long value) {
        return Predicates.compose(Predicates.equalTo(value), valueFunctions.getLong());
    }

    @Override
    public Predicate<Value> equalTo(Boolean value) {
        return Predicates.compose(Predicates.equalTo(value), valueFunctions.getBoolean());
    }

    @Override
    public Predicate<Value> equalTo(Calendar value) {
        return Predicates.compose(Predicates.equalTo(value), valueFunctions.getDate());
    }
}
