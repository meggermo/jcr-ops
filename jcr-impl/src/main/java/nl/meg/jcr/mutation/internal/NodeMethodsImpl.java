package nl.meg.jcr.mutation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.mutation.NodeMethods;
import nl.meg.jcr.validation.NodeValidators;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;
import nl.meg.validation.impl.ValidatorBuilderImpl;

import java.util.function.Function;
import java.util.function.Predicate;

import static nl.meg.validation.impl.ValidatingFunctionSupport.preValidate;

public final class NodeMethodsImpl implements NodeMethods {

    private final NodeValidators nodeValidators;
    private final Predicate<ValidationContext> terminationPredicate;

    public NodeMethodsImpl(NodeValidators nodeValidators, Predicate<ValidationContext> terminationPredicate) {
        this.nodeValidators = nodeValidators;
        this.terminationPredicate = terminationPredicate;
    }

    public NodeMethodsImpl(NodeValidators nodeValidators) {
        this(nodeValidators, ValidationContext::hasErrors);
    }

    @Override
    public Function<HippoNode, HippoNode> moveFunction(HippoNode newParent) {
        final Validator<HippoNode> validator = new ValidatorBuilderImpl<HippoNode>()
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canAddChild(newParent))
                .build(terminationPredicate);
        return preValidate(new MoveNodeImpl(newParent),validator);
    }

    @Override
    public Function<HippoNode, HippoNode> renameFunction(String newName) {
        final Validator<HippoNode> validator = new ValidatorBuilderImpl<HippoNode>()
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canRenameTo(newName))
                .build(terminationPredicate);
        return preValidate(new RenameNodeImpl(newName),validator);
    }

    @Override
    public Function<HippoNode, HippoNode> repositionFunction(int newPosition) {
        final Validator<HippoNode> validator = new ValidatorBuilderImpl<HippoNode>()
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.supportsOrdering())
                .add(nodeValidators.positionInBounds(newPosition))
                .build(terminationPredicate);
        return preValidate(new RepositionNodeImpl(newPosition),validator);
    }
}
