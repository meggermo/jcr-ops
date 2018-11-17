package nl.meg.jcr.store.internal;

import java.util.List;
import java.util.Optional;

public final class ControlConfig {

    private ReplicationState state;
    private Long lastSyncLogId;
    private List<String> syncRootPaths;

    public ReplicationState getState() {
        return state;
    }

    public void setState(final ReplicationState state) {
        this.state = state;
    }

    public Optional<Long> getLastSyncLogId() {
        return Optional.ofNullable(lastSyncLogId);
    }

    public void setLastSyncLogId(final Long lastSyncLogId) {
        this.lastSyncLogId = lastSyncLogId;
    }

    public Optional<List<String>> getSyncRootPaths() {
        return Optional.ofNullable(syncRootPaths);
    }

    public void setSyncRootPaths(final List<String> syncRootPaths) {
        this.syncRootPaths = syncRootPaths;
    }
}
