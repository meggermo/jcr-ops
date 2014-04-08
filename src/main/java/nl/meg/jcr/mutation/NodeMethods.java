package nl.meg.jcr.mutation;

import com.google.common.base.Function;
import nl.meg.jcr.INode;

public interface NodeMethods {

    Function<INode, INode> moveFunction(INode newParent);

    Function<INode, INode> renameFunction(String newName);

}
