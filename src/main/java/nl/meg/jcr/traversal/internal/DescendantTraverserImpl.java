package nl.meg.jcr.traversal.internal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Iterator;

import static com.google.common.collect.Iterators.emptyIterator;

class DescendantTraverserImpl extends TreeTraverser<Node> {

    @Override
    public Iterable<Node> children(final Node root) {
        return new Iterable<Node>() {
            @Override
            public Iterator<Node> iterator() {
                try {
                    return root.hasNodes() ? root.getNodes() : emptyIterator();
                } catch (RepositoryException e) {
                    throw new RuntimeRepositoryException(e);
                }
            }
        };
    }
}
