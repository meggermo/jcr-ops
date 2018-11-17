package nl.meg.jcr;


import java.util.Optional;
import java.util.stream.Stream;

import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.version.LabelExistsVersionException;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionHistory;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface HippoVersionHistory extends HippoItem<VersionHistory> {

    String getVersionableIdentifier();

    HippoVersion getRootVersion();

    Stream<HippoVersion> getAllLinearVersions();

    Stream<HippoVersion> getAllVersions();

    Stream<HippoNode> getAllLinearFrozenNodes();

    Stream<HippoNode> getAllFrozenNodes();

    Optional<HippoVersion> getVersion(String versionName);

    Optional<HippoVersion> getVersionByLabel(String label);

    HippoVersionHistory addVersionLabel(String versionName, String label) throws VersionException, LabelExistsVersionException;

    HippoVersionHistory addOrMoveVersionLabel(String versionName, String label) throws VersionException;

    HippoVersionHistory removeVersion(String versionName) throws UnsupportedRepositoryOperationException, VersionException;

    HippoVersionHistory removeVersionLabel(String label) throws VersionException;

    boolean hasVersionLabel(String label);

    boolean hasVersionLabel(HippoVersion version, String label);

    Stream<String> getVersionLabels();

    Stream<String> getVersionLabels(HippoVersion version);

}
