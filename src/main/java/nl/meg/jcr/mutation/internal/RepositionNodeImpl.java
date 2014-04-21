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
        final String sourceName = node.getName();
        final String targetName = getTargetName(node, nodes, nodes.indexOf(node));
        try {
            parent.get().orderBefore(sourceName, targetName);
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
        return node;
    }

    private String getTargetName(HippoNode node, List<HippoNode> nodes, int currentPosition) {
        if (newPosition < currentPosition) {
            return nodes.get(newPosition).getName();
        } else if (newPosition > currentPosition) {
            if (newPosition == nodes.size() - 1) {
                return null;
            } else {
                return nodes.get(newPosition + 1).getName();
            }
        } else {
            return node.getName();
        }
    }
}
