package nl.meg.jcr.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoVersion;
import nl.meg.jcr.HippoVersionHistory;

import javax.jcr.version.Version;
import javax.jcr.version.VersionException;
import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

final class HippoVersionImpl extends AbstractHippoItem<Version> implements HippoVersion {

    HippoVersionImpl(Version version) {
        super(version);
    }

    @Override
    public HippoVersionHistory getContainingHistory() {
        return versionHistory(invoke(Version::getContainingHistory));
    }

    @Override
    public Calendar getCreated() {
        return invoke(Version::getCreated);
    }

    @Override
    public Optional<HippoVersion> getLinearSuccessor() {
        return Optional.ofNullable(invoke(v -> {
            try {
                return version(v.getLinearSuccessor());
            } catch (VersionException e) {
                return null;
            }
        }));
    }

    @Override
    public Stream<HippoVersion> getSuccessors() {
        return stream(invoke(Version::getSuccessors)).map(this::version);
    }

    @Override
    public Optional<HippoVersion> getLinearPredecessor() {
        return Optional.ofNullable(invoke(v -> {
            try {
                return version(v.getLinearPredecessor());
            } catch (VersionException e) {
                return null;
            }
        }));
    }

    @Override
    public Stream<HippoVersion> getPredecessors() {
        return stream(invoke(Version::getPredecessors)).map(this::version);
    }

    @Override
    public HippoNode getFrozenNode() {
        return node(invoke(Version::getFrozenNode));
    }
}
