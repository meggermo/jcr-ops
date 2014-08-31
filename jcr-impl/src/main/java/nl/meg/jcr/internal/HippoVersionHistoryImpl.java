package nl.meg.jcr.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoVersion;
import nl.meg.jcr.HippoVersionHistory;
import nl.meg.jcr.RuntimeRepositoryException;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.version.*;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Arrays.stream;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.SIZED;
import static java.util.Spliterators.spliterator;

public class HippoVersionHistoryImpl extends AbstractHippoItem<VersionHistory> implements HippoVersionHistory {

    HippoVersionHistoryImpl(VersionHistory versionHistory) {
        super(versionHistory);
    }

    @Override
    public String getVersionableIdentifier() {
        return invoke(VersionHistory::getVersionableIdentifier);
    }

    @Override
    public HippoVersion getRootVersion() {
        return new HippoVersionImpl(invoke(VersionHistory::getRootVersion));
    }

    @Override
    public Stream<HippoVersion> getAllLinearVersions() {
        return invoke(vh -> {
            final VersionIterator vi = vh.getAllLinearVersions();
            final Spliterator<Version> sI = spliterator(vi, vi.getSize(), NONNULL | SIZED);
            return StreamSupport.stream(sI, false);
        }).map(this::version);
    }

    @Override
    public Stream<HippoVersion> getAllVersions() {
        return invoke(vh -> {
            final VersionIterator vi = vh.getAllVersions();
            final Spliterator<Version> sI = spliterator(vi, vi.getSize(), NONNULL | SIZED);
            return StreamSupport.stream(sI, false);
        }).map(this::version);
    }

    @Override
    public Stream<HippoNode> getAllLinearFrozenNodes() {
        return invoke(vh -> {
            final NodeIterator ni = vh.getAllLinearFrozenNodes();
            final Spliterator<Node> sI = spliterator(ni, ni.getSize(), NONNULL | SIZED);
            return StreamSupport.stream(sI, false);
        }).map(this::node);
    }

    @Override
    public Stream<HippoNode> getAllFrozenNodes() {
        return invoke(vh -> {
            final NodeIterator ni = vh.getAllFrozenNodes();
            final Spliterator<Node> sI = spliterator(ni, ni.getSize(), NONNULL | SIZED);
            return StreamSupport.stream(sI, false);
        }).map(this::node);
    }

    @Override
    public Optional<HippoVersion> getVersion(String versionName) {
        final Version v = invoke(vh -> {
            try {
                return vh.getVersion(versionName);
            } catch (VersionException e) {
                return null;
            }
        });
        return v != null ? Optional.of(version(v)) : Optional.empty();
    }

    @Override
    public Optional<HippoVersion> getVersionByLabel(String label) {
        final Version v = invoke(vh -> vh.hasVersionLabel(label) ? vh.getVersionByLabel(label) : null);
        return v != null ? Optional.of(version(v)) : Optional.empty();
    }

    @Override
    public HippoVersionHistory addVersionLabel(String versionName, String label) throws VersionException, LabelExistsVersionException {
        try {
            get().addVersionLabel(versionName, label, false);
            return new HippoVersionHistoryImpl(get());
        } catch (VersionException e) {
            throw e;
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public HippoVersionHistory addOrMoveVersionLabel(String versionName, String label) throws VersionException {
        try {
            get().addVersionLabel(versionName, label, true);
            return new HippoVersionHistoryImpl(get());
        } catch (VersionException e) {
            throw e;
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public HippoVersionHistory removeVersion(String versionName) throws UnsupportedRepositoryOperationException, VersionException {
        try {
            get().removeVersion(versionName);
            return new HippoVersionHistoryImpl(get());
        } catch (UnsupportedRepositoryOperationException | VersionException e) {
            throw e;
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public HippoVersionHistory removeVersionLabel(String label) throws VersionException {
        try {
            get().removeVersionLabel(label);
            return new HippoVersionHistoryImpl(get());
        } catch (VersionException e) {
            throw e;
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    @Override
    public boolean hasVersionLabel(String label) {
        return invoke(vh -> vh.hasVersionLabel(label));
    }

    @Override
    public boolean hasVersionLabel(Version version, String label) {
        return invoke(vh -> vh.hasVersionLabel(version, label));
    }

    @Override
    public Stream<String> getVersionLabels() {
        return stream(invoke(vh -> vh.getVersionLabels()));
    }

    @Override
    public Stream<String> getVersionLabels(Version version) {
        return stream(invoke(vh -> vh.getVersionLabels(version)));
    }
}
