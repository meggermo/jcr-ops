package nl.meg.jcr.internal;

import nl.meg.jcr.HippoValue;

import javax.jcr.Value;

final class HippoValueImpl implements HippoValue {

    private final Value value;

    HippoValueImpl(Value value) {
        this.value = value;
    }

    @Override
    public Value get() {
        return value;
    }
}
