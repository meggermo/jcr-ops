package nl.meg.jcr.traversal;

import aQute.bnd.annotation.ProviderType;

import java.util.stream.Stream;

@ProviderType
public interface NodeTraverser<T> {

    Stream<T> preOrderTraversal(T root);

    Stream<T> postOrderTraversal(T root);

    Stream<T> breadthFirstTraversal(T root);
}
