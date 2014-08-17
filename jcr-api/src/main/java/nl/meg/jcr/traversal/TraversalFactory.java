package nl.meg.jcr.traversal;

import nl.meg.jcr.HippoNode;

public interface TraversalFactory {

    NodeTraverser<HippoNode> ancestorTraverser();

    NodeTraverser<HippoNode> descendantTraverser();

    WhileIterables whileIterables();
}
