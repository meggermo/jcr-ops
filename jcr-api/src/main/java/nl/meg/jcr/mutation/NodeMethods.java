package nl.meg.jcr.mutation;

import aQute.bnd.annotation.ProviderType;
import nl.meg.jcr.HippoNode;

import java.util.function.Function;

@ProviderType
public interface NodeMethods {

    Function<HippoNode, HippoNode> moveFunction(HippoNode newParent);

    Function<HippoNode, HippoNode> renameFunction(String newName);

    Function<HippoNode, HippoNode> repositionFunction(int newPosition);
}
