package nl.meg.jcr.mutation.internal;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import nl.meg.jcr.INode;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import nl.meg.jcr.mutation.NodeMethods;

import javax.jcr.RepositoryException;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static java.lang.String.format;

public class NodeMethodsImpl implements NodeMethods {

    @Override
    public void move(INode node, INode newParent) {
            move(newParent, node, node.getName());
    }

    @Override
    public void rename(INode node, String newName) {
            final INode parent = node.getParent().get();
            if (supportsOrdering(parent)) {
                moveAndReorder(parent, node, newName);
            } else {
                move(parent, node, newName);
            }
    }

    private void move(INode parent, INode node, String newName) {
        final String parentPath = parent.getPath();
        final String sourceAbsPath = node.getPath();
        final String targetAbsPath = format("%s/%s", parentPath, newName);
        try {
            node.getSession().move(sourceAbsPath, targetAbsPath);
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    private void moveAndReorder(INode parent, INode node, String newName) {
        final ImmutableList<INode> nodes = ImmutableList.copyOf(parent.getNodes());
        int nodeIndex = Iterables.indexOf(nodes, compose(equalTo(node.getName()), GET_NAME));
        move(parent, node, newName);
        if (nodeIndex < nodes.size() - 1) {
            try {
                parent.get().orderBefore(newName, nodes.get(nodeIndex + 1).getName());
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    }

    private static boolean supportsOrdering(INode node) {
        return node.getPrimaryNodeType().hasOrderableChildNodes();
    }

    private static final Function<INode,String> GET_NAME = new Function<INode, String>() {
        @Override
        public String apply(INode input) {
            return input.getName();
        }
    };

}
