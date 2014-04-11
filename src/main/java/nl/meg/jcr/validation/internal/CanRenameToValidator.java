package nl.meg.jcr.validation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.PredicateBasedValidatorImpl;

import java.util.HashMap;
import java.util.Map;

final class CanRenameToValidator extends PredicateBasedValidatorImpl<NodeErrorCode, HippoNode> {

    private String name;

    CanRenameToValidator(final String name) {
        super(input -> !input.getParent().get().getNode(name).isPresent());
        this.name = name;
    }

    @Override
    protected Map<String, ?> getContextMap(HippoNode entity) {
        final Map<String, Object> map = new HashMap<>();
        map.put("path", entity.getParent().get().getPath());
        map.put("name", name);
        return map;
    }

    @Override
    protected NodeErrorCode getError() {
        return NodeErrorCode.ITEM_EXISTS;
    }
}
