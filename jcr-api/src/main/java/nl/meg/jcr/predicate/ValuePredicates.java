package nl.meg.jcr.predicate;

import java.util.Calendar;
import java.util.function.Predicate;

import org.osgi.annotation.versioning.ProviderType;

import nl.meg.jcr.HippoValue;

@ProviderType
public interface ValuePredicates {

    Predicate<HippoValue> equalTo(String value);

    Predicate<HippoValue> equalTo(Long value);

    Predicate<HippoValue> equalTo(Calendar value);

    Predicate<HippoValue> equalTo(Boolean value);

}
