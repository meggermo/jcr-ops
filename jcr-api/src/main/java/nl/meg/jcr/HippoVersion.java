package nl.meg.jcr;

import javax.jcr.version.Version;
import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Stream;

public interface HippoVersion extends HippoItem<Version> {

    public HippoVersionHistory getContainingHistory();

    public Calendar getCreated();

    public Optional<HippoVersion> getLinearSuccessor();

    public Stream<HippoVersion> getSuccessors();

    public Optional<HippoVersion> getLinearPredecessor();

    public Stream<HippoVersion> getPredecessors();

    public HippoNode getFrozenNode();
}
