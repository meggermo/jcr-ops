package nl.meg.jcr.store.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import nl.meg.jcr.store.JcrProperty;
import nl.meg.jcr.store.JcrRepo;
import nl.meg.jcr.store.JcrVersionedRepo;

public record ControlConfigRepo(
        String nodeType,
        JcrProperty<String> state,
        JcrProperty<Optional<Long>> lastSyncLogId,
        JcrRepo<SyncRoot> syncRootRepo
) implements JcrVersionedRepo<ControlConfig> {

    @Override
    public ControlConfig load(Node node, AtomicLong version) throws RepositoryException {
        final List<SyncRoot> syncRoots = new ArrayList<>();
        for (NodeIterator it = node.getNodes("SyncRoot-*"); it.hasNext(); ) {
            syncRoots.add(syncRootRepo.load(it.nextNode()));
        }
        return new ControlConfig(
                version,
                ReplicationState.valueOf(state.getValue(node)),
                lastSyncLogId.getValue(node).orElse(null),
                syncRoots
        );
    }

    @Override
    public void save(Node node, ControlConfig controlConfig) throws RepositoryException {
        state.setValue(node, controlConfig.state().name());
        lastSyncLogId.setValue(node, Optional.ofNullable(controlConfig.lastSyncLogId()));
        for (final SyncRoot syncRoot : controlConfig.syncRoots()) {
            if (!node.hasNode(syncRoot.relPath())) {
                node.addNode(syncRoot.relPath(), syncRootRepo.nodeType());
            }
            syncRootRepo.save(node.getNode(syncRoot.relPath()), syncRoot);
        }
    }
}
