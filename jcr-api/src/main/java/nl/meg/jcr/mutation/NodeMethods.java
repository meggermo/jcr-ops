package nl.meg.jcr.mutation;

import nl.meg.jcr.HippoNode;

import java.util.function.Function;

public interface NodeMethods {

    Function<HippoNode, HippoNode> moveFunction(HippoNode newParent);

    Function<HippoNode, HippoNode> renameFunction(String newName);

    Function<HippoNode, HippoNode> repositionFunction(int newPosition);
}
