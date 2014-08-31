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

public class HippoVersionImpl extends AbstractHippoItem<Version> implements HippoVersion {

    HippoVersionImpl(Version version) {
        super(version);
    }

    @Override
    public HippoVersionHistory getContainingHistory() {
        return new HippoVersionHistoryImpl(invoke(Version::getContainingHistory));
    }

    @Override
    public Calendar getCreated() {
        return invoke(Version::getCreated);
    }

    @Override
    public Optional<HippoVersion> getLinearSuccessor() {
        final Version succ = invoke(v -> {
            try {
                return v.getLinearSuccessor();

            } catch (VersionException e) {
                return null;
            }
        });
        return succ != null ? Optional.of(new HippoVersionImpl(succ)) : Optional.empty();
    }

    @Override
    public Stream<HippoVersion> getSuccessors() {
        return stream(invoke(Version::getSuccessors)).map(HippoVersionImpl::new);
    }

    @Override
    public Optional<HippoVersion> getLinearPredecessor() {
        final Version pred = invoke(v -> {
            try {
                return v.getLinearPredecessor();

            } catch (VersionException e) {
                return null;
            }
        });
        return pred != null ? Optional.of(new HippoVersionImpl(pred)) : Optional.empty();
    }

    @Override
    public Stream<HippoVersion> getPredecessors() {
        return stream(invoke(Version::getPredecessors)).map(HippoVersionImpl::new);
    }

    @Override
    public HippoNode getFrozenNode() {
        return new HippoNodeImpl(invoke(Version::getFrozenNode));
    }
}
