package nl.meg.cr;

import aQute.bnd.annotation.ProviderType;

import java.util.stream.Stream;

@ProviderType
public interface Property {
    Stream<Value> getValues();
}
