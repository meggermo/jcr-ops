package nl.meg.jcr.store.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import nl.meg.jcr.store.JcrNode;
import nl.meg.jcr.store.JcrProperty;

public abstract class AbstractJcrNode implements JcrNode {

    private static final JcrProperty<Optional<Long>> VERSION_PROPERTY = JcrPropertyFactory.ofLongOption("version");
    private final AtomicLong version;
    private final String absPath;

    public AbstractJcrNode(Node node) throws RepositoryException {
        this.absPath = node.getPath();
        this.version = new AtomicLong(VERSION_PROPERTY.getValue(node).orElse(0L));
    }

    @Override
    public final String getAbsPath() {
        return absPath;
    }

    @Override
    public Collection<JcrNode> getNodes() {
        return Collections.emptyList();
    }

    @Override
    public final JcrNode writeValues(Node node) throws RepositoryException {
        if (version.compareAndSet(VERSION_PROPERTY.getValue(node).orElse(0L), version.get() + 1)) {
            VERSION_PROPERTY.setValue(node, Optional.of(version.get()));
            for (JcrNode n : getNodes()) {
                writeValues(node.getNode(n.getAbsPath()));
            }
            doWriteValues(node);
        } else {
            final String message = String.format("Node at path %s was concurrently modified", absPath);
            throw new ConcurrentModificationException(message);
        }
        return this;
    }

    protected abstract void doWriteValues(Node node) throws RepositoryException;
}
