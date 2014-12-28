package nl.meg.jcr.traversal;

import java.util.stream.Stream;

public interface NodeTraverser<T> {

    Stream<T> preOrderTraversal(T root);

    Stream<T> postOrderTraversal(T root);

    Stream<T> breadthFirstTraversal(T root);
}
