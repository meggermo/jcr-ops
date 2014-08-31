package nl.meg.jcr.traversal.internal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.traversal.NodeTraverser;

import static java.util.stream.Collectors.toList;

class DescendantTraverserImpl extends TreeTraverser<HippoNode> implements NodeTraverser<HippoNode> {

    @Override
    public Iterable<HippoNode> children(final HippoNode root) {
        return root.getNodes().collect(toList());
    }
}
