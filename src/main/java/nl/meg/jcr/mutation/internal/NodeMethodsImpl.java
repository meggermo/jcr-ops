package nl.meg.jcr.mutation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.mutation.NodeMethods;
import nl.meg.jcr.validation.INodeValidators;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.Validator;
import nl.meg.validation.ValidatorBuilder;

import java.util.function.Function;

import static nl.meg.function.FunctionAdapter.preValidate;

public final class NodeMethodsImpl implements NodeMethods {

    private final INodeValidators nodeValidators;
    private final ValidatorBuilder<NodeErrorCode, HippoNode> validatorBuilder;

    public NodeMethodsImpl(INodeValidators nodeValidators, ValidatorBuilder<NodeErrorCode, HippoNode> validatorBuilder) {
        this.nodeValidators = nodeValidators;
        this.validatorBuilder = validatorBuilder;
    }

    @Override
    public Function<HippoNode, HippoNode> moveFunction(HippoNode newParent) {
        final Validator<NodeErrorCode, HippoNode> validator = validatorBuilder
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canAddChild(newParent))
                .build();
        return preValidate(validator, new MoveNodeImpl(newParent));
    }

    @Override
    public Function<HippoNode, HippoNode> renameFunction(String newName) {
        final Validator<NodeErrorCode, HippoNode> validator = validatorBuilder
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canRenameTo(newName))
                .build();
        return preValidate(validator, new RenameNodeImpl(newName));
    }

    @Override
    public Function<HippoNode, HippoNode> repositionFunction(int newPosition) {
        final Validator<NodeErrorCode, HippoNode> validator = validatorBuilder
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.supportsOrdering())
                .add(nodeValidators.positionInBounds(newPosition))
                .build();
        return preValidate(validator, new RepositionNodeImpl(newPosition));
    }
}
