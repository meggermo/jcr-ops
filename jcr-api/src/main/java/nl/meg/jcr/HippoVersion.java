package nl.meg.jcr;

import javax.jcr.version.Version;
import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Stream;

public interface HippoVersion extends HippoItem<Version> {

    HippoVersionHistory getContainingHistory();

    Calendar getCreated();

    Optional<HippoVersion> getLinearSuccessor();

    Stream<HippoVersion> getSuccessors();

    Optional<HippoVersion> getLinearPredecessor();

    Stream<HippoVersion> getPredecessors();

    HippoNode getFrozenNode();
}
