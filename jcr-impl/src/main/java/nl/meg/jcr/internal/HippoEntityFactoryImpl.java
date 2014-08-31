package nl.meg.jcr.internal;

import nl.meg.jcr.*;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Value;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;

public final class HippoEntityFactoryImpl implements HippoEntityFactory {

    @Override
    public HippoValue value(Value value) {
        return new HippoValueImpl(value);
    }

    @Override
    public HippoProperty property(Property property) {
        return new HippoPropertyImpl(property);
    }

    @Override
    public HippoNode node(Node node) {
        return new HippoNodeImpl(node);
    }

    @Override
    public HippoVersion version(Version version) {
        return new HippoVersionImpl(version);
    }

    @Override
    public HippoVersionHistory versionHistory(VersionHistory versionHistory) {
        return new HippoVersionHistoryImpl(versionHistory);
    }
}
