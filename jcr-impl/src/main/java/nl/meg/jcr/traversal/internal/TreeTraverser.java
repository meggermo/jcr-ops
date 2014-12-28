package nl.meg.jcr.traversal.internal;

import nl.meg.jcr.traversal.NodeTraverser;

import java.util.LinkedList;
import java.util.stream.Stream;

public abstract class TreeTraverser<T> implements NodeTraverser<T> {

    /**
     * Returns the children of the specified node.  Must not contain null.
     */
    public abstract Stream<T> children(T root);

    @Override
    public Stream<T> preOrderTraversal(T root) {
        return Stream.concat(Stream.of(root), children(root).flatMap(this::preOrderTraversal));
    }

    @Override
    public Stream<T> postOrderTraversal(T root) {
        return Stream.concat(children(root).flatMap(this::postOrderTraversal), Stream.of(root));
    }

    @Override
    public Stream<T> breadthFirstTraversal(T root) {
        final LinkedList<T> queue = new LinkedList<>();
        queue.add(root);
        final Stream.Builder<T> builder = Stream.builder();
        while(!queue.isEmpty()) {
            final T element = queue.remove();
            builder.accept(element);
            children(element).forEach(queue::add);
        }
        return builder.build();
    }
}
