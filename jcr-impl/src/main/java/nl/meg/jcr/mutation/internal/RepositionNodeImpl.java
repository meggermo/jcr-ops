package nl.meg.jcr.mutation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.RuntimeRepositoryException;

import javax.jcr.RepositoryException;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

final class RepositionNodeImpl implements Function<HippoNode, HippoNode> {

    private final int newPosition;

    RepositionNodeImpl(int newPosition) {
        this.newPosition = newPosition;
    }

    @Override
    public HippoNode apply(final HippoNode node) {
        final HippoNode parent = node.getParent().get();
        final List<HippoNode> nodes = parent.getNodes().collect(toList());
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
