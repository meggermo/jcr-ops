package nl.meg.jcr.traversal;

import aQute.bnd.annotation.ProviderType;
import nl.meg.jcr.HippoNode;

@ProviderType
public interface TraversalFactory {

    NodeTraverser<HippoNode> ancestorTraverser();

    NodeTraverser<HippoNode> descendantTraverser();

    WhileIterables whileIterables();
}
