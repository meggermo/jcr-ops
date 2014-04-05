package nl.meg.jcr.mutation.internal;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import nl.meg.jcr.INode;
import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.RepositoryException;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static java.lang.String.format;

final class RenameNodeImpl implements Function<INode, INode> {

    private final String newName;

    public RenameNodeImpl(String newName) {
        this.newName = newName;
    }

    @Override
    public INode apply(INode node) {
        final INode parent = node.getParent().get();
        if (supportsOrdering(parent)) {
            return moveAndReorder(parent, node);
        } else {
            return move(parent, node);
        }
    }

    INode move(INode parent, INode node) {
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

    private INode moveAndReorder(INode parent, INode node) {
        final ImmutableList<INode> nodes = ImmutableList.copyOf(parent.getNodes());
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

    private static boolean supportsOrdering(INode node) {
        return node.getPrimaryNodeType().hasOrderableChildNodes();
    }

    private static final Function<INode, String> GET_NAME = new Function<INode, String>() {
        @Override
        public String apply(INode input) {
            return input.getName();
        }
    };
}
