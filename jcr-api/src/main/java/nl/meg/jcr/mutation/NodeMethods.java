package nl.meg.jcr.mutation;

import java.util.function.Function;

import org.osgi.annotation.versioning.ProviderType;

import nl.meg.jcr.HippoNode;

@ProviderType
public interface NodeMethods {

    Function<HippoNode, HippoNode> moveFunction(HippoNode newParent);

    Function<HippoNode, HippoNode> renameFunction(String newName);

    Function<HippoNode, HippoNode> repositionFunction(int newPosition);
}
