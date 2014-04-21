package nl.meg.jcr.traversal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.HippoNode;

public interface TraversalFactory {

    TreeTraverser<HippoNode> ancestorTraverser();

    TreeTraverser<HippoNode> descendantTraverser();

    WhileIterables whileIterables();
}
