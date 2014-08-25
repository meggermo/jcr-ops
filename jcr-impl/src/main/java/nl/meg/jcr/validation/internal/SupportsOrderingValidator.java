package nl.meg.jcr.validation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.impl.PredicateBasedValidatorImpl;

final class SupportsOrderingValidator extends PredicateBasedValidatorImpl<HippoNode> {

    SupportsOrderingValidator() {
        super(input -> supportsOrdering(input.getParent().get()));
    }

    @Override
    protected String getCode() {
        return NodeErrorCode.ORDERING_NOT_SUPPORTED.toString();
    }

    @Override
    protected String getMessage() {
        return null;
    }

    private static boolean supportsOrdering(HippoNode node) {
        return node.getPrimaryNodeType().hasOrderableChildNodes();
    }
}
