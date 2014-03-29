package nl.meg.jcr.predicate.internal;

import com.google.common.base.Predicate;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import nl.meg.jcr.function.NodeFunctions;
import nl.meg.jcr.predicate.NodePredicates;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

import java.util.Iterator;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.in;
import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Iterators.*;

final class NodePredicatesImpl implements NodePredicates {

    private final NodeFunctions nodeFunctions;

    NodePredicatesImpl(NodeFunctions nodeFunctions) {
        this.nodeFunctions = nodeFunctions;
    }

    @Override
    public Predicate<Node> isSame(final Node other) {
        return new Predicate<Node>() {
            @Override
            public boolean apply(Node node) {
                try {
                    return other.isSame(node);
                } catch (RepositoryException e) {
                    throw new RuntimeRepositoryException(e);
                }
            }
        };
    }

    @Override
    public Predicate<Node> isNodeType(final String nodeTypeName) {
        return new Predicate<Node>() {
            @Override
            public boolean apply(Node input) {
                try {
                    return input.isNodeType(nodeTypeName);
                } catch (RepositoryException e) {
                    throw new RuntimeRepositoryException(e);
                }
            }
        };
    }

    @Override
    public Predicate<Node> identifierIn(String... identifiers) {
        return compose(in(copyOf(identifiers)), nodeFunctions.getIdentifier());
    }

    @Override
    public Predicate<Node> nameIn(String... names) {
        return compose(in(copyOf(names)), nodeFunctions.getName());
    }

    @Override
    public Predicate<Node> withProperty(final Predicate<Property> predicate) {
        return new Predicate<Node>() {
            @Override
            public boolean apply(Node node) {
                return tryFind(nodeFunctions.getProperties().apply(node), predicate).isPresent();
            }
        };
    }

    @Override
    public Predicate<Node> withNodeType(final Predicate<NodeType> predicate) {
        return new Predicate<Node>() {
            @Override
            public boolean apply(final Node node) {
                final Iterator<NodeType> nodeTypeIterator = concat(
                        nodeFunctions.getMixinNodeTypes().apply(node),
                        singletonIterator(nodeFunctions.getPrimaryNodeType().apply(node)));
                return tryFind(nodeTypeIterator, predicate).isPresent();
            }
        };
    }

}
