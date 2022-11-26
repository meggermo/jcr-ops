package nl.meg.jcr.store.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import nl.meg.jcr.store.JcrVersioned;

public record ControlConfig(
        AtomicLong version,
        ReplicationState state,
        Long lastSyncLogId,
        List<SyncRoot> syncRoots) implements JcrVersioned {

    public ControlConfig copy(ReplicationState replicationState) {
        return new ControlConfig(this.version, replicationState, this.lastSyncLogId, this.syncRoots);
    }

    public ControlConfig copy(List<SyncRoot> syncRoots) {
        return new ControlConfig(this.version, this.state, this.lastSyncLogId, syncRoots);
    }

}
