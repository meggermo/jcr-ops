package nl.meg.jcr.traversal;

/**
 * Created by michiel on 8/17/14.
 */
public interface NodeTraverser<T> {
    public Iterable<T> children(final T root);
}
