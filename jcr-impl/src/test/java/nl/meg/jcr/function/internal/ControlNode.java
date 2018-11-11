package nl.meg.jcr.function.internal;

import java.util.List;
import java.util.Optional;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import nl.meg.jcr.function.JcrProperty;
import static nl.meg.jcr.function.JcrPropertyFactory.ofLongOption;
import static nl.meg.jcr.function.JcrPropertyFactory.ofString;
import static nl.meg.jcr.function.JcrPropertyFactory.ofStringListOption;

public final class ControlNode {

    private static final JcrProperty<String> STATE =
            ofString("state");

    private static final JcrProperty<Optional<List<String>>> SYNC_ROOTS =
            ofStringListOption("syncRoots");

    private static final JcrProperty<Optional<Long>> LAST_SYNC_LOG_ID =
            ofLongOption("lastSyncLogId");

    public static ControlConfig getConfig(Node node) throws RepositoryException {
        final ControlConfig controlConfig = new ControlConfig();
        controlConfig.setState(ReplicationState.valueOf(STATE.getValue(node)));
        LAST_SYNC_LOG_ID.getValue(node)
                .ifPresent(controlConfig::setLastSyncLogId);
        SYNC_ROOTS.getValue(node)
                .ifPresent(controlConfig::setSyncRootPaths);
        return controlConfig;
    }

    public static void update(Node node, ControlConfig controlConfig) throws RepositoryException {
        STATE.setValue(node, controlConfig.getState().name());
        LAST_SYNC_LOG_ID.setValue(node, controlConfig.getLastSyncLogId());
        SYNC_ROOTS.setValue(node, controlConfig.getSyncRootPaths());
    }
}
