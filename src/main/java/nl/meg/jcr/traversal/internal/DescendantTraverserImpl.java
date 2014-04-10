package nl.meg.jcr.traversal.internal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.INode;

class DescendantTraverserImpl extends TreeTraverser<INode> {

    @Override
    public Iterable<INode> children(final INode root) {
        return () -> root.getNodes();
    }
}
