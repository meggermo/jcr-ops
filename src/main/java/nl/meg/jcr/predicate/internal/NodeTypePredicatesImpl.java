package nl.meg.jcr.predicate.internal;

import com.google.common.base.Predicate;
import nl.meg.jcr.function.NodeTypeFunctions;
import nl.meg.jcr.predicate.NodeTypePredicates;

import javax.jcr.nodetype.NodeType;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.in;
import static com.google.common.collect.ImmutableSet.copyOf;

final class NodeTypePredicatesImpl implements NodeTypePredicates {

    private final NodeTypeFunctions nodeTypeFunctions;

    NodeTypePredicatesImpl(NodeTypeFunctions nodeTypeFunctions) {
        this.nodeTypeFunctions = nodeTypeFunctions;
    }

    @Override
    public Predicate<NodeType> nodeTypeIn(String... nodeTypeNames) {
        return compose(in(copyOf(nodeTypeNames)), nodeTypeFunctions.getName());
    }
}
