package nl.meg.jcr.store.internal;


import java.util.concurrent.atomic.AtomicLong;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import nl.meg.jcr.store.JcrProperty;
import nl.meg.jcr.store.JcrVersionedRepo;

public record SyncRootRepo(String nodeType, JcrProperty<String> syncRootPath) implements JcrVersionedRepo<SyncRoot> {

    @Override
    public SyncRoot load(Node node, AtomicLong version) throws RepositoryException {
        return new SyncRoot(version, node.getName(), syncRootPath.getValue(node));
    }

    @Override
    public void save(Node node, SyncRoot syncRoot) throws RepositoryException {
        syncRootPath.setValue(node, syncRoot.syncRootPath());
    }
}
