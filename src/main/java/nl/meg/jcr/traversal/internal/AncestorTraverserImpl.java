package nl.meg.jcr.traversal.internal;

import com.google.common.collect.Iterators;
import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.INode;

import java.util.Optional;

import static com.google.common.collect.Iterators.singletonIterator;

final class AncestorTraverserImpl extends TreeTraverser<INode> {

    @Override
    public Iterable<INode> children(final INode root) {
        return () -> {
            final Optional<INode> parent = root.getParent();
            return parent.isPresent() ? singletonIterator(parent.get()) : Iterators.<INode>emptyIterator();
        };
    }
}
