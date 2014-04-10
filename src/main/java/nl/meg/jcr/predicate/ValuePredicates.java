package nl.meg.jcr.predicate;

import javax.jcr.Value;
import java.util.Calendar;
import java.util.function.Predicate;

public interface ValuePredicates {

    Predicate<Value> equalTo(String value);

    Predicate<Value> equalTo(Long value);

    Predicate<Value> equalTo(Calendar value);

    Predicate<Value> equalTo(Boolean value);

}
