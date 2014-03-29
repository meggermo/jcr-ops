package nl.meg.jcr.predicate;

import com.google.common.base.Predicate;

import javax.jcr.Value;
import java.util.Calendar;

public interface ValuePredicates {

    Predicate<Value> equalTo(String value);

    Predicate<Value> equalTo(Long value);

    Predicate<Value> equalTo(Calendar value);

    Predicate<Value> equalTo(Boolean value);

}
