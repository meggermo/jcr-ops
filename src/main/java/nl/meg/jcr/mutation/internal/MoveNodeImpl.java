package nl.meg.jcr.mutation.internal;

import nl.meg.jcr.HippoNode;

import java.util.function.Function;

final class MoveNodeImpl implements Function<HippoNode, HippoNode> {

    private final HippoNode newParent;

    MoveNodeImpl(HippoNode newParent) {
        this.newParent = newParent;
    }

    @Override
    public HippoNode apply(HippoNode node) {
        return new RenameNodeImpl(node.getName()).move(newParent, node);
    }
}
