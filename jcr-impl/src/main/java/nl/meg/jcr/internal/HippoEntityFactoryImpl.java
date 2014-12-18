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
        assert value != null : "value cannot be null";
        return new HippoValueImpl(value);
    }

    @Override
    public HippoProperty property(Property property) {
        assert property != null : "property cannot be null";
        return new HippoPropertyImpl(property);
    }

    @Override
    public HippoNode node(Node node) {
        assert node != null : "node cannot be null";
        return new HippoNodeImpl(node);
    }

    @Override
    public HippoVersion version(Version version) {
        assert version != null : "version cannot be null";
        return new HippoVersionImpl(version);
    }

    @Override
    public HippoVersionHistory versionHistory(VersionHistory versionHistory) {
        assert versionHistory != null : "versionHistory cannot be null";
        return new HippoVersionHistoryImpl(versionHistory);
    }

    @Override
    public MutableHippoNode mutableNode(Node node) {
        assert node != null : "node cannot be null";
        return new MutableHippoNodeImpl(node);
    }
}
