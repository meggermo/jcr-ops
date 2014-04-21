package nl.meg.jcr.traversal.internal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.traversal.TraversalFactory;
import nl.meg.jcr.traversal.WhileIterables;


public final class TraversalFactoryImpl implements TraversalFactory {

    private final TreeTraverser<HippoNode> ancestorTraverser;
    private final TreeTraverser<HippoNode> descendantTraverser;
    private final WhileIterables whileIterables;

    public TraversalFactoryImpl(TreeTraverser<HippoNode> ancestorTraverser, TreeTraverser<HippoNode> descendantTraverser, WhileIterables whileIterables) {
        this.ancestorTraverser = ancestorTraverser;
        this.descendantTraverser = descendantTraverser;
        this.whileIterables = whileIterables;
    }

    public TraversalFactoryImpl() {
        this(new AncestorTraverserImpl(), new DescendantTraverserImpl(), new WhileIterablesImpl());
    }

    @Override
    public TreeTraverser<HippoNode> ancestorTraverser() {
        return ancestorTraverser;
    }

    @Override
    public TreeTraverser<HippoNode> descendantTraverser() {
        return descendantTraverser;
    }

    @Override
    public WhileIterables whileIterables() {
        return whileIterables;
    }
}
