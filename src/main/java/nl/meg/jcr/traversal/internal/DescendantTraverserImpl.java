package nl.meg.jcr.traversal.internal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.INode;

import java.util.Iterator;

class DescendantTraverserImpl extends TreeTraverser<INode> {

    @Override
    public Iterable<INode> children(final INode root) {
        return new Iterable<INode>() {
            @Override
            public Iterator<INode> iterator() {
                return root.getNodes();
            }
        };
    }
}
