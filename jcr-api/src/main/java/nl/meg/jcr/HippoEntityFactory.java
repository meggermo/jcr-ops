package nl.meg.jcr;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Value;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;

public interface HippoEntityFactory {

    HippoValue value(Value value);

    HippoProperty property(Property property);

    HippoNode node(Node node);

    HippoVersion version(Version version);

    HippoVersionHistory versionHistory(VersionHistory versionHistory);

    MutableHippoNode mutableNode(Node node);
}
