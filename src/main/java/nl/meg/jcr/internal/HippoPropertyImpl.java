package nl.meg.jcr.internal;

import nl.meg.jcr.HippoProperty;

import javax.jcr.Property;

public class HippoPropertyImpl extends AbstractHippoItem<Property> implements HippoProperty{

    HippoPropertyImpl(Property property) {
        super(property, HippoNodeImpl::new);
    }
}
