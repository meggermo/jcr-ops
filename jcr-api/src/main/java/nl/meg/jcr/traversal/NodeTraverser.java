package nl.meg.jcr.traversal;

public interface NodeTraverser<T> {

    Iterable<T> preOrderTraversal(T root);

    Iterable<T> postOrderTraversal(T root);

    Iterable<T> breadthFirstTraversal(T root);
}
