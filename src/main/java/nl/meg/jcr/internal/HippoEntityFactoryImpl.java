package nl.meg.jcr.internal;

import nl.meg.jcr.HippoEntityFactory;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.HippoValue;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Value;

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
}
