package nl.meg.jcr.traversal;


import java.util.stream.Stream;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface NodeTraverser<T> {

    Stream<T> preOrderTraversal(T root);

    Stream<T> postOrderTraversal(T root);

    Stream<T> breadthFirstTraversal(T root);
}
