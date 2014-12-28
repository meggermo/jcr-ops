package nl.meg.jcr.traversal.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.traversal.NodeTraverser;

import java.util.stream.Stream;

final class AncestorTraverserImpl extends TreeTraverser<HippoNode> implements NodeTraverser<HippoNode> {

    @Override
    public Stream<HippoNode> children(HippoNode root) {
        return root.getParent()
                .map(Stream::of)
                .orElse(Stream.empty());
    }

}
