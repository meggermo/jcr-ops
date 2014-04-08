package nl.meg.jcr.mutation.internal;

import com.google.common.base.Function;
import nl.meg.function.ValidatingFunctionAdapter;
import nl.meg.jcr.INode;
import nl.meg.jcr.mutation.NodeMethods;
import nl.meg.jcr.validation.INodeValidators;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.Validator;
import nl.meg.validation.ValidatorBuilder;

public class NodeMethodsImpl implements NodeMethods {

    private final INodeValidators nodeValidators;
    private final ValidatorBuilder<NodeErrorCode, INode> validatorBuilder;
    private final ValidatingFunctionAdapter<NodeErrorCode, INode, INode> adapter;

    public NodeMethodsImpl(INodeValidators nodeValidators, ValidatorBuilder<NodeErrorCode, INode> validatorBuilder, ValidatingFunctionAdapter<NodeErrorCode, INode, INode> adapter) {
        this.nodeValidators = nodeValidators;
        this.validatorBuilder = validatorBuilder;
        this.adapter = adapter;
    }

    @Override
    public Function<INode, INode> moveFunction(INode newParent) {
        final Validator<NodeErrorCode, INode> validator = validatorBuilder
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canAddChild(newParent))
                .build();
        final Function<INode, INode> function = new MoveNodeImpl(newParent);
        return adapter.adapt(validator, function);
    }

    @Override
    public Function<INode, INode> renameFunction(String newName) {
        final Validator<NodeErrorCode, INode> validator = validatorBuilder
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canRenameTo(newName))
                .build();
        final Function<INode, INode> function = new RenameNodeImpl(newName);
        return adapter.adapt(validator, function);
    }

}
