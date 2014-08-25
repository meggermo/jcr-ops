package nl.meg.jcr;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Value;

public interface HippoEntityFactory {

    HippoValue value(Value value);

    HippoProperty property(Property property);

    HippoNode node(Node node);
}
