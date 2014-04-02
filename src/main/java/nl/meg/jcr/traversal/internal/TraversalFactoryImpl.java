package nl.meg.jcr.traversal.internal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.INode;
import nl.meg.jcr.traversal.TraversalFactory;
import nl.meg.jcr.traversal.WhileIterables;


public final class TraversalFactoryImpl implements TraversalFactory {

    private final TreeTraverser<INode> ancestorTraverser;
    private final TreeTraverser<INode> descendantTraverser;
    private final WhileIterables whileIterables;

    public TraversalFactoryImpl(TreeTraverser<INode> ancestorTraverser, TreeTraverser<INode> descendantTraverser, WhileIterables whileIterables) {
        this.ancestorTraverser = ancestorTraverser;
        this.descendantTraverser = descendantTraverser;
        this.whileIterables = whileIterables;
    }

    public TraversalFactoryImpl() {
        this(new AncestorTraverserImpl(), new DescendantTraverserImpl(), new WhileIterablesImpl());
    }

    @Override
    public TreeTraverser<INode> ancestorTraverser() {
        return ancestorTraverser;
    }

    @Override
    public TreeTraverser<INode> descendantTraverser() {
        return descendantTraverser;
    }

    @Override
    public WhileIterables whileIterables() {
        return whileIterables;
    }
}
