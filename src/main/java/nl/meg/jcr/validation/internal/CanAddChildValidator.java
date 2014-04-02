package nl.meg.jcr.validation.internal;

import com.google.common.base.Predicate;
import nl.meg.jcr.INode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.PredicateBasedValidatorImpl;

final class CanAddChildValidator extends PredicateBasedValidatorImpl<NodeErrorCode, INode> {

    CanAddChildValidator(final INode parent) {
        super(new Predicate<INode>() {
            @Override
            public boolean apply(INode node) {
                return parent.getPrimaryNodeType().canAddChildNode(node.getName(), node.getPrimaryNodeType().getName());
            }
        });
    }

    @Override
    protected NodeErrorCode getError() {
        return NodeErrorCode.CANNOT_ADD_CHILD;
    }
}
