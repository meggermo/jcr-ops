package nl.meg.jcr;

import javax.jcr.Property;
import java.util.List;
import java.util.Optional;

public interface HippoProperty extends HippoItem<Property> {

    boolean isMultiple();

    Optional<HippoValue> getValue();

    List<HippoValue> getValues();

}
