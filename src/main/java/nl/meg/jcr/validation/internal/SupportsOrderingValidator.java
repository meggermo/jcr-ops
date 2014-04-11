package nl.meg.jcr.validation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.PredicateBasedValidatorImpl;

final class SupportsOrderingValidator extends PredicateBasedValidatorImpl<NodeErrorCode, HippoNode> {

    SupportsOrderingValidator() {
        super(input -> supportsOrdering(input.getParent().get()));
    }

    @Override
    protected NodeErrorCode getError() {
        return NodeErrorCode.ORDERING_NOT_SUPPORTED;
    }

    private static boolean supportsOrdering(HippoNode node) {
        return node.getPrimaryNodeType().hasOrderableChildNodes();
    }

}
