package nl.meg.jcr.predicate;

import nl.meg.jcr.HippoValue;

import java.util.Calendar;
import java.util.function.Predicate;

public interface ValuePredicates {

    Predicate<HippoValue> equalTo(String value);

    Predicate<HippoValue> equalTo(Long value);

    Predicate<HippoValue> equalTo(Calendar value);

    Predicate<HippoValue> equalTo(Boolean value);

}
