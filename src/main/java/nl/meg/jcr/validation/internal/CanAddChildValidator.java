package nl.meg.jcr.validation.internal;

import nl.meg.jcr.INode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.PredicateBasedValidatorImpl;

final class CanAddChildValidator extends PredicateBasedValidatorImpl<NodeErrorCode, INode> {

    CanAddChildValidator(final INode parent) {
        super(node -> parent.getPrimaryNodeType().canAddChildNode(node.getName(), node.getPrimaryNodeType().getName()));
    }

    @Override
    protected NodeErrorCode getError() {
        return NodeErrorCode.CANNOT_ADD_CHILD;
    }
}
