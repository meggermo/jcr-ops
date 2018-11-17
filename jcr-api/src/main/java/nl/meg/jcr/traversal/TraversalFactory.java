package nl.meg.jcr.traversal;


import org.osgi.annotation.versioning.ProviderType;

import nl.meg.jcr.HippoNode;

@ProviderType
public interface TraversalFactory {

    NodeTraverser<HippoNode> ancestorTraverser();

    NodeTraverser<HippoNode> descendantTraverser();

    WhileIterables whileIterables();
}
