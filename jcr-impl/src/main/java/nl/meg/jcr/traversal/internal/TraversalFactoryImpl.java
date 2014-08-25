package nl.meg.jcr.traversal.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.traversal.NodeTraverser;
import nl.meg.jcr.traversal.TraversalFactory;
import nl.meg.jcr.traversal.WhileIterables;


public final class TraversalFactoryImpl implements TraversalFactory {

    private final NodeTraverser<HippoNode> ancestorTraverser;
    private final NodeTraverser<HippoNode> descendantTraverser;
    private final WhileIterables whileIterables;

    public TraversalFactoryImpl(NodeTraverser<HippoNode> ancestorTraverser, NodeTraverser<HippoNode> descendantTraverser, WhileIterables whileIterables) {
        this.ancestorTraverser = ancestorTraverser;
        this.descendantTraverser = descendantTraverser;
        this.whileIterables = whileIterables;
    }

    public TraversalFactoryImpl() {
        this(new AncestorTraverserImpl(), new DescendantTraverserImpl(), new WhileIterablesImpl());
    }

    @Override
    public NodeTraverser<HippoNode> ancestorTraverser() {
        return ancestorTraverser;
    }

    @Override
    public NodeTraverser<HippoNode> descendantTraverser() {
        return descendantTraverser;
    }

    @Override
    public WhileIterables whileIterables() {
        return whileIterables;
    }
}
