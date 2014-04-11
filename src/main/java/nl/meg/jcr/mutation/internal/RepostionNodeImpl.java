package nl.meg.jcr.mutation.internal;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.RepositoryException;
import java.util.List;

final class RepostionNodeImpl implements Function<HippoNode, HippoNode> {

    private final int newPosition;

    RepostionNodeImpl(int newPosition) {
        this.newPosition = newPosition;
    }

    @Override
    public HippoNode apply(final HippoNode node) {
        final HippoNode parent = node.getParent().get();
        final List<HippoNode> nodes = ImmutableList.copyOf(parent.getNodes());
        final int currentPosition = Iterables.indexOf(nodes, new Predicate<HippoNode>() {
            @Override
            public boolean apply(HippoNode input) {
                return input.isSame(node);
            }
        });
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
            parent.get().orderBefore(sourceName,targetName);
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
        return node;
    }
}
