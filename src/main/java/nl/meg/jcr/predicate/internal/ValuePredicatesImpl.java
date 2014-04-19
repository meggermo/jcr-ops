package nl.meg.jcr.predicate.internal;

import nl.meg.jcr.HippoValue;
import nl.meg.jcr.predicate.ValuePredicates;

import java.util.Calendar;
import java.util.function.Predicate;

final class ValuePredicatesImpl implements ValuePredicates {

    @Override
    public Predicate<HippoValue> equalTo(String value) {
        return val -> value.equals(val.getString());
    }

    @Override
    public Predicate<HippoValue> equalTo(Long value) {
        return val -> value.equals(val.getLong());
    }

    @Override
    public Predicate<HippoValue> equalTo(Boolean value) {
        return val -> value.equals(val.getBoolean());
    }

    @Override
    public Predicate<HippoValue> equalTo(Calendar value) {
        return val -> value.equals(val.getDate());
    }
}
