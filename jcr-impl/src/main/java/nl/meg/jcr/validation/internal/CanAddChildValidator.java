package nl.meg.jcr.validation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.impl.PredicateBasedValidatorImpl;

final class CanAddChildValidator extends PredicateBasedValidatorImpl<HippoNode> {

    CanAddChildValidator(final HippoNode parent) {
        super(node -> parent.getPrimaryNodeType().canAddChildNode(node.getName(), node.getPrimaryNodeType().getName()));
    }

    @Override
    protected String getCode() {
        return NodeErrorCode.CANNOT_ADD_CHILD.toString();
    }

    @Override
    protected String getMessage() {
        return null;
    }
}
