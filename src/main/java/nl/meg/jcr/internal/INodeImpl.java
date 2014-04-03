package nl.meg.jcr.internal;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterators;
import nl.meg.jcr.INode;
import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.util.Collections;
import java.util.Iterator;

final class INodeImpl implements INode, Function<Node, INode> {

    private final Node node;

    INodeImpl(Node node) {
        this.node = node;
    }

    @Override
    public String getName() {
        try {
            return node.getName();
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public String getPath() {
        try {
            return node.getPath();
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public Session getSession() {
        try {
            return node.getSession();
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public boolean isSame(INode otherNode) {
        try {
            return node.isSame(otherNode.get());
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public Optional<INode> getNode(String name) {
        try {
            return node.hasNode(name) ? Optional.<INode>of(apply(node.getNode(name))) : Optional.<INode>absent();
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public NodeType getPrimaryNodeType() {
        try {
            return node.getPrimaryNodeType();
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public Optional<INode> getParent() {
        try {
            return Optional.of(apply(node.getParent()));
        } catch (ItemNotFoundException e) {
            return Optional.absent();
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public Iterator<INode> getNodes() {
        try {
            return node.hasNodes() ? Iterators.transform(node.getNodes(), this) : Collections.emptyIterator();
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public boolean isNodeType(String nodeTypeName) {
        try {
            return node.isNodeType(nodeTypeName);
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public Optional<Property> getProperty(String name) {
        try {
            return node.hasProperty(name) ? Optional.<Property>of(node.getProperty(name)) : Optional.<Property>absent();
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public String getIdentifier() {
        try {
            return node.getIdentifier();
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public Iterator<Property> getProperties() {
        try {
            return node.hasProperties() ? node.getProperties() : Collections.emptyIterator();
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public Integer getIndex() {
        try {
            return node.getIndex();
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public NodeType[] getMixinNodeTypes() {
        try {
            return node.getMixinNodeTypes();
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public boolean isRoot() {
        try {
            return node.getSession().getRootNode().isSame(node);
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
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
