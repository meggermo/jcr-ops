package nl.meg.jcr.validation.internal;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.PredicateBasedValidatorImpl;

import java.util.Map;

import static com.google.common.collect.Iterators.size;

final class PositionInBoundsValidator extends PredicateBasedValidatorImpl<NodeErrorCode, HippoNode> {

    private final int newPosition;

    PositionInBoundsValidator(final int newPosition) {
        super(node -> positionInRange(node.getParent().get(), newPosition));
        this.newPosition = newPosition;
    }

    @Override
    protected Map<String, ?> getContextMap(HippoNode entity) {
        final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        return builder
                .put("min", 0)
                .put("position", newPosition)
                .put("max", size(entity.getParent().get().getNodes()) - 1)
                .build();
    }

    @Override
    protected NodeErrorCode getError() {
        return NodeErrorCode.POSITION_OUT_OF_RANGE;
    }

    private static boolean positionInRange(HippoNode node, int postion) {
        return Range.closedOpen(0, size(node.getNodes())).contains(postion);
    }
}
