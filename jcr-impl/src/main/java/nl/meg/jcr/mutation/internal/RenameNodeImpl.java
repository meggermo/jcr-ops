package nl.meg.jcr.mutation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.RuntimeRepositoryException;

import javax.jcr.RepositoryException;
import java.util.List;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

final class RenameNodeImpl implements Function<HippoNode, HippoNode> {

    private final String newName;

    RenameNodeImpl(String newName) {
        this.newName = newName;
    }

    @Override
    public HippoNode apply(HippoNode node) {
        final HippoNode parent = node.getParent().get();
        if (supportsOrdering(parent)) {
            return moveAndReorder(parent, node);
        } else {
            return move(parent, node);
        }
    }

    HippoNode move(HippoNode parent, HippoNode node) {
        final String parentPath = parent.getPath();
        final String sourceAbsPath = node.getPath();
        final String targetAbsPath = format("%s/%s", parentPath, newName);
        try {
            node.getSession().move(sourceAbsPath, targetAbsPath);
            return node;
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    private HippoNode moveAndReorder(HippoNode parent, HippoNode node) {
        final List<HippoNode> nodes = parent.getNodes().collect(toList());
        final int nodeIndex = nodes.indexOf(node);
        move(parent, node);
        if (nodeIndex < nodes.size() - 1) {
            try {
                parent.get().orderBefore(newName, nodes.get(nodeIndex + 1).getName());
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
        return node;
    }

    private static boolean supportsOrdering(HippoNode node) {
        return node.getPrimaryNodeType().hasOrderableChildNodes();
    }

}
