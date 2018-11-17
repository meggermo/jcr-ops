package nl.meg.jcr;


import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Stream;

import javax.jcr.version.Version;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface HippoVersion extends HippoItem<Version> {

    HippoVersionHistory getContainingHistory();

    Calendar getCreated();

    Optional<HippoVersion> getLinearSuccessor();

    Stream<HippoVersion> getSuccessors();

    Optional<HippoVersion> getLinearPredecessor();

    Stream<HippoVersion> getPredecessors();

    HippoNode getFrozenNode();
}
