package nl.meg.jcr.predicate.internal;


import nl.meg.jcr.predicate.NodeTypePredicates;

import javax.jcr.nodetype.NodeType;
import java.util.function.Predicate;
import java.util.stream.Stream;

final class NodeTypePredicatesImpl implements NodeTypePredicates {

    @Override
    public Predicate<NodeType> nodeTypeIn(String... nodeTypeNames) {
        return n -> Stream.of(nodeTypeNames).anyMatch(name -> name.equals(n.getPrimaryItemName()));
    }
}
