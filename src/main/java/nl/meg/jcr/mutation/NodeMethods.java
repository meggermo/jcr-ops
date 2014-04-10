package nl.meg.jcr.mutation;

import nl.meg.function.ValidatingFunction;
import nl.meg.jcr.INode;

public interface NodeMethods {

    ValidatingFunction<INode, INode> moveFunction(INode newParent);

    ValidatingFunction<INode, INode> renameFunction(String newName);

    ValidatingFunction<INode, INode> repositionFunction(int newPosition);
}
