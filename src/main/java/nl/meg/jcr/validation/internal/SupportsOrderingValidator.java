package nl.meg.jcr.validation.internal;

import nl.meg.jcr.INode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.PredicateBasedValidatorImpl;

final class SupportsOrderingValidator extends PredicateBasedValidatorImpl<NodeErrorCode, INode> {

    SupportsOrderingValidator() {
        super(input -> supportsOrdering(input.getParent().get()));
    }

    @Override
    protected NodeErrorCode getError() {
        return NodeErrorCode.ORDERING_NOT_SUPPORTED;
    }

    private static boolean supportsOrdering(INode node) {
        return node.getPrimaryNodeType().hasOrderableChildNodes();
    }

}
