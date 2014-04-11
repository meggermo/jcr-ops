package nl.meg.jcr.validation.internal;

import com.google.common.collect.ImmutableMap;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.PredicateBasedValidatorImpl;

import java.util.Map;

final class CanRenameToValidator extends PredicateBasedValidatorImpl<NodeErrorCode, HippoNode> {

    private String name;

    CanRenameToValidator(final String name) {
        super(input -> !input.getParent().get().getNode(name).isPresent());
        this.name = name;
    }

    @Override
    protected Map<String, ?> getContextMap(HippoNode entity) {
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
