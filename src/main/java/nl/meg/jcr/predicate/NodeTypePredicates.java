package nl.meg.jcr.predicate;

import com.google.common.base.Predicate;

import javax.jcr.nodetype.NodeType;

public interface NodeTypePredicates {

    Predicate<NodeType> nodeTypeIn(String... nodeTypeNames);
}
