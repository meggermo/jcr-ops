package nl.meg.jcr.internal;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Value;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;

import org.osgi.service.component.annotations.Component;

import nl.meg.jcr.HippoEntityFactory;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.HippoValue;
import nl.meg.jcr.HippoVersion;
import nl.meg.jcr.HippoVersionHistory;
import nl.meg.jcr.MutableHippoNode;

@Component(name = "HippoEntityFactory", immediate = true)
public final class HippoEntityFactoryImpl implements HippoEntityFactory {

    @Override
    public HippoValue value(Value value) {
        assert value != null : "value cannot be null";
        return new HippoValueImpl(value);
    }

    @Override
    public HippoProperty property(Property property) {
        assert property != null : "property cannot be null";
        return new HippoPropertyImpl(property, this);
    }

    @Override
    public HippoNode node(Node node) {
        assert node != null : "node cannot be null";
        return new HippoNodeImpl(node, this);
    }

    @Override
    public HippoVersion version(Version version) {
        assert version != null : "version cannot be null";
        return new HippoVersionImpl(version, this);
    }

    @Override
    public HippoVersionHistory versionHistory(VersionHistory versionHistory) {
        assert versionHistory != null : "versionHistory cannot be null";
        return new HippoVersionHistoryImpl(versionHistory, this);
    }

    @Override
    public MutableHippoNode mutableNode(Node node) {
        assert node != null : "node cannot be null";
        return new MutableHippoNodeImpl(node, this);
    }
}
