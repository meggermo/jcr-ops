package nl.meg.cr;

import aQute.bnd.annotation.ProviderType;

import java.util.Optional;

@ProviderType
public interface Session {

    Optional<Node> getNode(String absPath);
}
