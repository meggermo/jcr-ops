package nl.meg.jcr.mutation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.RepositoryException;
import java.util.List;
import java.util.function.Function;

final class RepositionNodeImpl implements Function<HippoNode, HippoNode> {

    private final int newPosition;

    RepositionNodeImpl(int newPosition) {
        this.newPosition = newPosition;
    }

    @Override
    public HippoNode apply(final HippoNode node) {
        final HippoNode parent = node.getParent().get();
        final List<HippoNode> nodes = parent.getNodes();
        final int currentPosition = nodes.indexOf(node);
        final String sourceName = node.getName();
        final String targetName;
        if (newPosition < currentPosition) {
            targetName = nodes.get(newPosition).getName();
        } else if (newPosition > currentPosition) {
            if (newPosition == nodes.size() - 1) {
                targetName = null;
            } else {
                targetName = nodes.get(newPosition + 1).getName();
            }
        } else {
            targetName = node.getName();
        }
        try {
            parent.get().orderBefore(sourceName, targetName);
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
        return node;
    }
}
