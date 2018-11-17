package nl.meg.jcr.predicate;


import java.util.function.Predicate;

import javax.jcr.nodetype.NodeType;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface NodeTypePredicates {

    Predicate<NodeType> nodeTypeIn(String... nodeTypeNames);
}
