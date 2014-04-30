package nl.meg.jcr.validation.internal;

import com.google.common.collect.Range;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.PredicateBasedValidatorImpl;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

final class PositionInBoundsValidator extends PredicateBasedValidatorImpl<NodeErrorCode, HippoNode> {

    private final int newPosition;

    PositionInBoundsValidator(final int newPosition) {
        super(node -> positionInRange(node.getParent().get(), newPosition));
        this.newPosition = newPosition;
    }

    @Override
    protected Map<String, ?> getContextMap(HippoNode entity) {
        final Map<String, Object> map = new HashMap<>();
        map.put("min", 0);
        map.put("position", newPosition);
        map.put("max", entity.getParent().get().getNodesAsStream().collect(toList()).size() - 1);
        return map;
    }

    @Override
    protected NodeErrorCode getError() {
        return NodeErrorCode.POSITION_OUT_OF_RANGE;
    }

    private static boolean positionInRange(HippoNode node, int postion) {
        return Range.closedOpen(0, node.getNodesAsStream().collect(toList()).size()).contains(postion);
    }
}
