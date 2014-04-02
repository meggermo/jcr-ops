package nl.meg.jcr.predicate.internal;

import com.google.common.base.Predicate;
import nl.meg.jcr.INode;
import nl.meg.jcr.function.NodeFunctions;
import nl.meg.jcr.predicate.NodePredicates;

import javax.jcr.Property;
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
    public Predicate<INode> isSame(final INode other) {
        return new Predicate<INode>() {
            @Override
            public boolean apply(INode node) {
                return other.isSame(node);
            }
        };
    }

    @Override
    public Predicate<INode> isNodeType(final String nodeTypeName) {
        return new Predicate<INode>() {
            @Override
            public boolean apply(INode input) {
                return input.isNodeType(nodeTypeName);
            }
        };
    }

    @Override
    public Predicate<INode> identifierIn(String... identifiers) {
        return compose(in(copyOf(identifiers)), nodeFunctions.getIdentifier());
    }

    @Override
    public Predicate<INode> nameIn(String... names) {
        return compose(in(copyOf(names)), nodeFunctions.getName());
    }

    @Override
    public Predicate<INode> withProperty(final Predicate<Property> predicate) {
        return new Predicate<INode>() {
            @Override
            public boolean apply(INode node) {
                return tryFind(nodeFunctions.getProperties().apply(node), predicate).isPresent();
            }
        };
    }

    @Override
    public Predicate<INode> withNodeType(final Predicate<NodeType> predicate) {
        return new Predicate<INode>() {
            @Override
            public boolean apply(final INode node) {
                final Iterator<NodeType> nodeTypeIterator = concat(
                        nodeFunctions.getMixinNodeTypes().apply(node),
                        singletonIterator(nodeFunctions.getPrimaryNodeType().apply(node)));
                return tryFind(nodeTypeIterator, predicate).isPresent();
            }
        };
    }

}
