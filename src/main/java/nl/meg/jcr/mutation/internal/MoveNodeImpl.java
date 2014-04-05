package nl.meg.jcr.mutation.internal;

import com.google.common.base.Function;
import nl.meg.jcr.INode;

final class MoveNodeImpl implements Function<INode,INode> {

    private final INode newParent;

    public MoveNodeImpl(INode newParent) {
        this.newParent = newParent;
    }

    @Override
    public INode apply(INode node) {
        return new RenameNodeImpl(node.getName()).move(newParent, node);
    }
}
