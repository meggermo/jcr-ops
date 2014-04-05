package nl.meg.jcr.mutation;

import nl.meg.jcr.INode;

public interface NodeMethods {

    INode move(INode node, INode newParent);

    INode rename(INode node, String newName);

}
