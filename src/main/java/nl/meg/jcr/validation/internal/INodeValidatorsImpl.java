package nl.meg.jcr.validation.internal;

import nl.meg.jcr.INode;
import nl.meg.jcr.validation.INodeValidators;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.Validator;

public final class INodeValidatorsImpl implements INodeValidators {

    private static final Validator<NodeErrorCode, INode> IS_NOT_ROOT = new IsNotRootValidator();

    @Override
    public Validator<NodeErrorCode, INode> isNotRoot() {
        return IS_NOT_ROOT;
    }

    @Override
    public Validator<NodeErrorCode, INode> canAddChild(INode parent) {
        return new CanAddChildValidator(parent);
    }

    @Override
    public Validator<NodeErrorCode, INode> canRenameTo(String name) {
        return new CanRenameToValidator(name);
    }
}