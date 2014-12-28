package nl.meg.jcr.traversal.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.traversal.NodeTraverser;

import java.util.stream.Stream;

class DescendantTraverserImpl extends TreeTraverser<HippoNode> implements NodeTraverser<HippoNode> {

    @Override
    public Stream<HippoNode> children(HippoNode root) {
        return root.getNodes();
    }
}
