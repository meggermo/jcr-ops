package nl.meg.jcr.validation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeValidators;
import nl.meg.validation.Validator;

public final class NodeValidatorsImpl implements NodeValidators {

    private static final Validator<HippoNode> IS_NOT_ROOT = new IsNotRootValidator();
    private static final Validator<HippoNode> SUPPORTS_ORDERING = new SupportsOrderingValidator();

    @Override
    public Validator<HippoNode> isNotRoot() {
        return IS_NOT_ROOT;
    }

    @Override
    public Validator<HippoNode> canAddChild(HippoNode parent) {
        return new CanAddChildValidator(parent);
    }

    @Override
    public Validator<HippoNode> canRenameTo(String name) {
        return new CanRenameToValidator(name);
    }

    @Override
    public Validator<HippoNode> positionInBounds(int newPosition) {
        return new PositionInBoundsValidator(newPosition);
    }

    @Override
    public Validator<HippoNode> supportsOrdering() {
        return SUPPORTS_ORDERING;
    }
}
