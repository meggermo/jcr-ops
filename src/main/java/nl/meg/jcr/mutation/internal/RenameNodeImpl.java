package nl.meg.jcr.mutation.internal;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.RepositoryException;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static java.lang.String.format;

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
        final ImmutableList<HippoNode> nodes = ImmutableList.copyOf(parent.getNodes());
        int nodeIndex = Iterables.indexOf(nodes, compose(equalTo(node.getName()), GET_NAME));
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

    private static final Function<HippoNode, String> GET_NAME = new Function<HippoNode, String>() {
        @Override
        public String apply(HippoNode input) {
            return input.getName();
        }
    };
}
