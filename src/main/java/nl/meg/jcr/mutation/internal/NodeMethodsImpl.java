package nl.meg.jcr.mutation.internal;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import nl.meg.jcr.INode;
import nl.meg.jcr.mutation.NodeMethods;
import nl.meg.jcr.validation.INodeValidators;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.function.ValidatingFunctionAdapter;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;
import nl.meg.validation.ValidatorBuilder;

public class NodeMethodsImpl implements NodeMethods {

    private final INodeValidators nodeValidators;
    private final Supplier<ValidationContext<NodeErrorCode, INode>> contextSupplier;
    private final Predicate<ValidationContext<NodeErrorCode, INode>> continueValidation;

    public NodeMethodsImpl(INodeValidators nodeValidators, Supplier<ValidationContext<NodeErrorCode, INode>> contextSupplier, Predicate<ValidationContext<NodeErrorCode, INode>> continueValidation) {
        this.nodeValidators = nodeValidators;
        this.contextSupplier = contextSupplier;
        this.continueValidation = continueValidation;
    }

    @Override
    public INode move(INode node, INode newParent) {
        final Validator<NodeErrorCode, INode> validator = builder()
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canAddChild(newParent))
                .build();
        final Function<INode, INode> function = new MoveNodeImpl(newParent);
        return validate(validator, function).apply(node);
    }

    @Override
    public INode rename(INode node, String newName) {
        final Validator<NodeErrorCode, INode> validator = builder()
                .add(nodeValidators.isNotRoot())
                .add(nodeValidators.canRenameTo(newName))
                .build();
        final Function<INode, INode> function = new RenameNodeImpl(newName);
        return validate(validator, function).apply(node);
    }

    private Function<INode, INode> validate(Validator<NodeErrorCode, INode> validator, Function<INode, INode> function) {
        return new ValidatingFunctionAdapter<>(contextSupplier, validator, function);
    }

    private ValidatorBuilder<NodeErrorCode, INode> builder() {
        return ValidatorBuilder.builder(continueValidation);
    }

}
