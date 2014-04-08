package nl.meg.jcr.mutation.internal;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import nl.meg.function.ValidatingFunctionAdapter;
import nl.meg.jcr.INode;
import nl.meg.jcr.mutation.NodeMethods;
import nl.meg.jcr.validation.INodeValidators;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;
import nl.meg.validation.ValidatorBuilder;

public class NodeMethodsImpl implements NodeMethods {

    private final INodeValidators nodeValidators;
    private final Predicate<ValidationContext<NodeErrorCode, INode>> continueValidation;
    private final ValidatingFunctionAdapter<NodeErrorCode, INode, INode> adapter;

    public NodeMethodsImpl(INodeValidators nodeValidators, ValidatingFunctionAdapter<NodeErrorCode, INode, INode> adapter, Predicate<ValidationContext<NodeErrorCode, INode>> continueValidation) {
        this.nodeValidators = nodeValidators;
        this.continueValidation = continueValidation;
        this.adapter = adapter;
    }

    @Override
    public INode move(INode node, INode newParent) {
        final Validator<NodeErrorCode, INode> validator = builder()
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canAddChild(newParent))
                .build();
        final Function<INode, INode> function = new MoveNodeImpl(newParent);
        return adapter.adapt(validator, function).apply(node);
    }

    @Override
    public INode rename(INode node, String newName) {
        final Validator<NodeErrorCode, INode> validator = builder()
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canRenameTo(newName))
                .build();
        final Function<INode, INode> function = new RenameNodeImpl(newName);
        return adapter.adapt(validator, function).apply(node);
    }

    private ValidatorBuilder<NodeErrorCode, INode> builder() {
        return ValidatorBuilder.builder(continueValidation);
    }

}
