package nl.meg.jcr.mutation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.mutation.NodeMethods;

import java.util.function.Function;

public final class NodeMethodsImpl implements NodeMethods {

    @Override
    public Function<HippoNode, HippoNode> moveFunction(HippoNode newParent) {
        return new MoveNodeImpl(newParent);
    }

    @Override
    public Function<HippoNode, HippoNode> renameFunction(String newName) {
        return new RenameNodeImpl(newName);
    }

    @Override
    public Function<HippoNode, HippoNode> repositionFunction(int newPosition) {
        return new RepositionNodeImpl(newPosition);
    }
}
