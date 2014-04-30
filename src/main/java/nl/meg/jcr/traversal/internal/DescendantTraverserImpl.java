package nl.meg.jcr.traversal.internal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.HippoNode;

import static java.util.stream.Collectors.toList;

class DescendantTraverserImpl extends TreeTraverser<HippoNode> {

    @Override
    public Iterable<HippoNode> children(final HippoNode root) {
        return root.getNodesAsStream().collect(toList());
    }
}
