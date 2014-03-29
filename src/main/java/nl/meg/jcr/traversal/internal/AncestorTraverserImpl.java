package nl.meg.jcr.traversal.internal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Iterator;

import static com.google.common.collect.Iterators.emptyIterator;
import static com.google.common.collect.Iterators.singletonIterator;

final class AncestorTraverserImpl extends TreeTraverser<Node> {

    @Override
    public Iterable<Node> children(final Node root) {
        return new Iterable<Node>() {
            @Override
            public Iterator<Node> iterator() {
                try {
                    return singletonIterator(root.getParent());
                } catch (ItemNotFoundException e) {
                    return emptyIterator();
                } catch (RepositoryException e) {
                    throw new RuntimeRepositoryException(e);
                }
            }
        };
    }
}
