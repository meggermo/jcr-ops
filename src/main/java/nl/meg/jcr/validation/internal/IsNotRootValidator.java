package nl.meg.jcr.validation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.PredicateBasedValidatorImpl;

import java.util.function.Predicate;

final class IsNotRootValidator extends PredicateBasedValidatorImpl<NodeErrorCode, HippoNode> {

    IsNotRootValidator() {
        super(IS_NOT_ROOT);
    }

    @Override
    protected NodeErrorCode getError() {
        return NodeErrorCode.NODE_CANNOT_BE_ROOT;
    }

    private static final Predicate<HippoNode> IS_NOT_ROOT = node -> !node.isRoot();

}
