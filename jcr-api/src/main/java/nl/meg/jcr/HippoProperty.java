package nl.meg.jcr;

import aQute.bnd.annotation.ProviderType;

import javax.jcr.Property;
import java.util.Optional;
import java.util.stream.Stream;

@ProviderType
public interface HippoProperty extends HippoItem<Property> {

    boolean isMultiple();

    Optional<HippoValue> getValue();

    Stream<HippoValue> getValues();

}
