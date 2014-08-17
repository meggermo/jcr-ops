package nl.meg.jcr.validation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.impl.PredicateBasedValidatorImpl;

import java.util.function.Predicate;

final class IsNotRootValidator extends PredicateBasedValidatorImpl<HippoNode> {

    private static final Predicate<HippoNode> IS_NOT_ROOT = node -> !node.isRoot();

    IsNotRootValidator() {
        super(IS_NOT_ROOT);
    }

    @Override
    protected String getCode() {
        return NodeErrorCode.NODE_CANNOT_BE_ROOT.toString();
    }

    @Override
    protected String getMessage() {
        return null;
    }
}
