package nl.meg.jcr.internal;

import nl.meg.jcr.INode;

import javax.jcr.Node;

final class INodeImpl implements INode {

    private final Node node;

    INodeImpl(Node node) {
        this.node = node;
    }

    @Override
    public Node get() {
        return node;
    }

    @Override
    public INode apply(Node aNode) {
        return new INodeImpl(aNode);
    }
}
