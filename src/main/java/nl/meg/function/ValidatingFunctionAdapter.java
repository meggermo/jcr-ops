package nl.meg.function;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;

public class ValidatingFunctionAdapter<E extends Enum<E>, S, T> implements Function<S, T> {

    private final Supplier<ValidationContext<E, S>> contextSupplier;
    private final Validator<E, S> validator;
    private final Function<S, T> function;

    public ValidatingFunctionAdapter(Supplier<ValidationContext<E, S>> contextSupplier, Validator<E, S> validator, Function<S, T> function) {
        this.contextSupplier = contextSupplier;
        this.validator = validator;
        this.function = function;
    }

    @Override
    public T apply(S node) {
        final ValidationContext<E, S> context = validator.validate(node, contextSupplier.get());
        if (context.isValid()) {
            return function.apply(node);
        } else {
            throw new ValidationException(context.getErrors());
        }
    }
}
