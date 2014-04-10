package nl.meg.jcr.mutation.internal;

import nl.meg.function.ValidatingFunction;
import nl.meg.function.ValidatingFunctionAdapter;
import nl.meg.jcr.INode;
import nl.meg.jcr.mutation.NodeMethods;
import nl.meg.jcr.validation.INodeValidators;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.Validator;
import nl.meg.validation.ValidatorBuilder;

public final class NodeMethodsImpl implements NodeMethods {

    private final INodeValidators nodeValidators;
    private final ValidatorBuilder<NodeErrorCode, INode> validatorBuilder;
    private final ValidatingFunctionAdapter<NodeErrorCode, INode, INode> adapter;

    public NodeMethodsImpl(INodeValidators nodeValidators, ValidatorBuilder<NodeErrorCode, INode> validatorBuilder, ValidatingFunctionAdapter<NodeErrorCode, INode, INode> adapter) {
        this.nodeValidators = nodeValidators;
        this.validatorBuilder = validatorBuilder;
        this.adapter = adapter;
    }

    @Override
    public ValidatingFunction<INode, INode> moveFunction(INode newParent) {
        final Validator<NodeErrorCode, INode> validator = validatorBuilder
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canAddChild(newParent))
                .build();
        return adapter.adapt(validator, new MoveNodeImpl(newParent));
    }

    @Override
    public ValidatingFunction<INode, INode> renameFunction(String newName) {
        final Validator<NodeErrorCode, INode> validator = validatorBuilder
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canRenameTo(newName))
                .build();
        return adapter.adapt(validator, new RenameNodeImpl(newName));
    }

    @Override
    public ValidatingFunction<INode, INode> repositionFunction(int newPosition) {
        final Validator<NodeErrorCode, INode> validator = validatorBuilder
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.supportsOrdering())
                .add(nodeValidators.positionInBounds(newPosition))
                .build();
        return adapter.adapt(validator, new RepostionNodeImpl(newPosition));
    }
}
