package nl.meg.jcr.validation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.PredicateBasedValidatorImpl;

final class CanAddChildValidator extends PredicateBasedValidatorImpl<NodeErrorCode, HippoNode> {

    CanAddChildValidator(final HippoNode parent) {
        super(node -> parent.getPrimaryNodeType().canAddChildNode(node.getName(), node.getPrimaryNodeType().getName()));
    }

    @Override
    protected NodeErrorCode getError() {
        return NodeErrorCode.CANNOT_ADD_CHILD;
    }
}
