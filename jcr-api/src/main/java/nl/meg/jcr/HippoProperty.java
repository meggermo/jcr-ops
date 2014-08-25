package nl.meg.jcr;

import javax.jcr.Property;
import java.util.Optional;
import java.util.stream.Stream;

public interface HippoProperty extends HippoItem<Property> {

    boolean isMultiple();

    Optional<HippoValue> getValue();

    Stream<HippoValue> getValues();

}
