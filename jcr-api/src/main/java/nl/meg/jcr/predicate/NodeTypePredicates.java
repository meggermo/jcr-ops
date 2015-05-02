package nl.meg.jcr.predicate;


import aQute.bnd.annotation.ProviderType;

import javax.jcr.nodetype.NodeType;
import java.util.function.Predicate;

@ProviderType
public interface NodeTypePredicates {

    Predicate<NodeType> nodeTypeIn(String... nodeTypeNames);
}
