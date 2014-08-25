package nl.meg.jcr.validation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.impl.PredicateBasedValidatorImpl;

import java.util.HashMap;
import java.util.Map;

final class CanRenameToValidator extends PredicateBasedValidatorImpl<HippoNode> {

    private final String name;

    CanRenameToValidator(final String name) {
        super(input -> !input.getParent().get().getNode(name).isPresent());
        this.name = name;
    }

    @Override
    protected String getCode() {
        return NodeErrorCode.ITEM_EXISTS.toString();
    }

    @Override
    protected String getMessage() {
        return null;
    }

    @Override
    protected Map<String, ?> getContextMap(HippoNode entity) {
        final Map<String, Object> map = new HashMap<>();
        map.put("path", entity.getParent().get().getPath());
        map.put("name", name);
        return map;
    }

}