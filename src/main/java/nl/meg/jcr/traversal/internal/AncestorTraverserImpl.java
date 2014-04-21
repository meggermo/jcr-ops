package nl.meg.jcr.traversal.internal;

import com.google.common.collect.Iterators;
import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.HippoNode;

import java.util.Optional;

import static com.google.common.collect.Iterators.singletonIterator;

final class AncestorTraverserImpl extends TreeTraverser<HippoNode> {

    @Override
    public Iterable<HippoNode> children(final HippoNode root) {
        return () -> {
            final Optional<HippoNode> parent = root.getParent();
            return parent.isPresent() ? singletonIterator(parent.get()) : Iterators.<HippoNode>emptyIterator();
        };
    }
}
