package nl.meg.jcr.traversal.internal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.traversal.TraversalFactory;
import nl.meg.jcr.traversal.WhileIterables;

import javax.jcr.Node;

public final class TraversalFactoryImpl implements TraversalFactory {

    private final TreeTraverser<Node> ancestorTraverser;
    private final TreeTraverser<Node> descendantTraverser;
    private final WhileIterables whileIterables;

    public TraversalFactoryImpl(TreeTraverser<Node> ancestorTraverser, TreeTraverser<Node> descendantTraverser, WhileIterables whileIterables) {
        this.ancestorTraverser = ancestorTraverser;
        this.descendantTraverser = descendantTraverser;
        this.whileIterables = whileIterables;
    }

    public TraversalFactoryImpl() {
        this(new AncestorTraverserImpl(), new DescendantTraverserImpl(), new WhileIterablesImpl());
    }

    @Override
    public TreeTraverser<Node> ancestorTraverser() {
        return ancestorTraverser;
    }

    @Override
    public TreeTraverser<Node> descendantTraverser() {
        return descendantTraverser;
    }

    @Override
    public WhileIterables whileIterables() {
        return whileIterables;
    }
}
