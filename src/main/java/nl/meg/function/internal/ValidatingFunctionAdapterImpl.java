package nl.meg.function.internal;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import nl.meg.function.ValidatingFunctionAdapter;
import nl.meg.function.ValidationException;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;

final class ValidatingFunctionAdapterImpl<E extends Enum<E>,S,T> implements ValidatingFunctionAdapter<E,S,T> {

    private final Supplier<ValidationContext<E, S>> contextSupplier;

    public ValidatingFunctionAdapterImpl(Supplier<ValidationContext<E, S>> contextSupplier) {
        this.contextSupplier = contextSupplier;
    }

    @Override
    public Function<S, T> adapt(Validator<E, S> validator, Function<S, T> function) {
        return new Adapter(contextSupplier, validator, function);
    }

    private final class Adapter implements Function<S, T> {

        private final Supplier<ValidationContext<E, S>> contextSupplier;
        private final Validator<E, S> validator;
        private final Function<S, T> function;

        private Adapter(Supplier<ValidationContext<E, S>> contextSupplier, Validator<E, S> validator, Function<S, T> function) {
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
}
