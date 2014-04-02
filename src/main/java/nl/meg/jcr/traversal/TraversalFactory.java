package nl.meg.jcr.traversal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.INode;

public interface TraversalFactory {

    TreeTraverser<INode> ancestorTraverser();

    TreeTraverser<INode> descendantTraverser();

    WhileIterables whileIterables();
}
