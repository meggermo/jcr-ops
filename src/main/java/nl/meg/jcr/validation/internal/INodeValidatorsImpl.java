package nl.meg.jcr.validation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.INodeValidators;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.Validator;

public final class INodeValidatorsImpl implements INodeValidators {

    private static final Validator<NodeErrorCode, HippoNode> IS_NOT_ROOT = new IsNotRootValidator();
    private static final Validator<NodeErrorCode, HippoNode> SUPPORTS_ORDERING = new SupportsOrderingValidator();

    @Override
    public Validator<NodeErrorCode, HippoNode> isNotRoot() {
        return IS_NOT_ROOT;
    }

    @Override
    public Validator<NodeErrorCode, HippoNode> canAddChild(HippoNode parent) {
        return new CanAddChildValidator(parent);
    }

    @Override
    public Validator<NodeErrorCode, HippoNode> canRenameTo(String name) {
        return new CanRenameToValidator(name);
    }

    @Override
    public Validator<NodeErrorCode, HippoNode> positionInBounds(int newPosition) {
        return new PositionInBoundsValidator(newPosition);
    }

    @Override
    public Validator<NodeErrorCode, HippoNode> supportsOrdering() {
        return SUPPORTS_ORDERING;
    }
}
