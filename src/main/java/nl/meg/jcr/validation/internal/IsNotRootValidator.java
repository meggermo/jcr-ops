package nl.meg.jcr.validation.internal;

import com.google.common.base.Predicate;
import nl.meg.jcr.INode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.PredicateBasedValidatorImpl;

final class IsNotRootValidator extends PredicateBasedValidatorImpl<NodeErrorCode, INode> {

    IsNotRootValidator() {
        super(IS_NOT_ROOT);
    }

    @Override
    protected NodeErrorCode getError() {
        return NodeErrorCode.NODE_CANNOT_BE_ROOT;
    }

    private static final Predicate<INode> IS_NOT_ROOT = new Predicate<INode>() {
        @Override
        public boolean apply(INode node) {
            return !node.isRoot();
        }
    };

}
