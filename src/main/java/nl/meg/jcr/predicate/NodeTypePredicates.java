package nl.meg.jcr.predicate;


import javax.jcr.nodetype.NodeType;
import java.util.function.Predicate;

public interface NodeTypePredicates {

    Predicate<NodeType> nodeTypeIn(String... nodeTypeNames);
}
