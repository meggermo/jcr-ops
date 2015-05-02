package nl.meg.cr;

import aQute.bnd.annotation.ProviderType;

import java.util.stream.Stream;

@ProviderType
public interface Node {

    Stream<Node> getNodes ();

    Stream<Property> getProperties();
}
