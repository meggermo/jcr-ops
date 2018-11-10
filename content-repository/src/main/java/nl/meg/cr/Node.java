package nl.meg.cr;


import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface Node {

    Stream<Node> getNodes();

    <T> Optional<T> getValue(String propertyName, Class<T> type);

    <T> Optional<List<T>> getValues(String propertyName, Class<T> type);
}
