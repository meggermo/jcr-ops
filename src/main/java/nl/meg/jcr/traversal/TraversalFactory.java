package nl.meg.jcr.traversal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.traversal.WhileIterables;

import javax.jcr.Node;

public interface TraversalFactory {

    TreeTraverser<Node> ancestorTraverser();

    TreeTraverser<Node> descendantTraverser();

    WhileIterables whileIterables();
}
