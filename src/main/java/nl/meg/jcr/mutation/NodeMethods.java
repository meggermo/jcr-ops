package nl.meg.jcr.mutation;

import nl.meg.jcr.INode;

public interface NodeMethods {

    void move(INode node, INode newParent);

    void rename(INode node, String newName);

}
