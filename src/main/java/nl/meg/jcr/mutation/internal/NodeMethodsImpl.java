package nl.meg.jcr.mutation.internal;

import nl.meg.function.FunctionAdapter;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.mutation.NodeMethods;
import nl.meg.jcr.validation.INodeValidators;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.Validator;
import nl.meg.validation.ValidatorBuilder;

import java.util.function.Function;

public final class NodeMethodsImpl implements NodeMethods {

    private final INodeValidators nodeValidators;
    private final ValidatorBuilder<NodeErrorCode, HippoNode> validatorBuilder;
    private final FunctionAdapter<NodeErrorCode, HippoNode, HippoNode> adapter;

    public NodeMethodsImpl(INodeValidators nodeValidators, ValidatorBuilder<NodeErrorCode, HippoNode> validatorBuilder, FunctionAdapter<NodeErrorCode, HippoNode, HippoNode> adapter) {
        this.nodeValidators = nodeValidators;
        this.validatorBuilder = validatorBuilder;
        this.adapter = adapter;
    }

    @Override
    public Function<HippoNode, HippoNode> moveFunction(HippoNode newParent) {
        final Validator<NodeErrorCode, HippoNode> validator = validatorBuilder
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canAddChild(newParent))
                .build();
        return adapter.preValidate(validator, new MoveNodeImpl(newParent));
    }

    @Override
    public Function<HippoNode, HippoNode> renameFunction(String newName) {
        final Validator<NodeErrorCode, HippoNode> validator = validatorBuilder
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canRenameTo(newName))
                .build();
        return adapter.preValidate(validator, new RenameNodeImpl(newName));
    }

    @Override
    public Function<HippoNode, HippoNode> repositionFunction(int newPosition) {
        final Validator<NodeErrorCode, HippoNode> validator = validatorBuilder
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.supportsOrdering())
                .add(nodeValidators.positionInBounds(newPosition))
                .build();
        return adapter.preValidate(validator, new RepostionNodeImpl(newPosition));
    }
}
