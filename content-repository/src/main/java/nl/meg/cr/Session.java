package nl.meg.cr;


import java.util.Optional;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface Session {

    Optional<Node> getNode(String absPath);
}
