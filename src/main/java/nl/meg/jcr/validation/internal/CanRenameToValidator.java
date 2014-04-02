package nl.meg.jcr.validation.internal;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import nl.meg.jcr.INode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.PredicateBasedValidatorImpl;

import java.util.Map;

final class CanRenameToValidator extends PredicateBasedValidatorImpl<NodeErrorCode, INode> {

    private String name;

    CanRenameToValidator(final String name) {
        super(new Predicate<INode>() {
            @Override
            public boolean apply(INode input) {
                return !input.getParent().get().hasNode(name);
            }
        });
        this.name = name;
    }

    @Override
    protected Map<String, ?> getContextMap(INode entity) {
        final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        return builder
                .put("path", entity.getParent().get().getPath())
                .put("name", name)
                .build();
    }

    @Override
    protected NodeErrorCode getError() {
        return NodeErrorCode.ITEM_EXISTS;
    }
}
