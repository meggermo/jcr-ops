package nl.meg.jcr;


import java.util.Optional;
import java.util.stream.Stream;

import javax.jcr.Property;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface HippoProperty extends HippoItem<Property> {

    boolean isMultiple();

    Optional<HippoValue> getValue();

    Stream<HippoValue> getValues();

}
