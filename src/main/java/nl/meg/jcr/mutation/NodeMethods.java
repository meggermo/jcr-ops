package nl.meg.jcr.mutation;

import nl.meg.function.ValidatingFunction;
import nl.meg.jcr.HippoNode;

public interface NodeMethods {

    ValidatingFunction<HippoNode, HippoNode> moveFunction(HippoNode newParent);

    ValidatingFunction<HippoNode, HippoNode> renameFunction(String newName);

    ValidatingFunction<HippoNode, HippoNode> repositionFunction(int newPosition);
}
