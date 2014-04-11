package nl.meg.function.internal;

import nl.meg.function.ValidatingFunction;
import nl.meg.function.ValidatingFunctionAdapter;
import nl.meg.function.ValidationException;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;

import java.util.function.Function;
import java.util.function.Supplier;

final class ValidatingFunctionAdapterImpl<E extends Enum<E>, S, T> implements ValidatingFunctionAdapter<E, S, T> {

    private final Supplier<ValidationContext<E, S>> contextSupplier;

    public ValidatingFunctionAdapterImpl(Supplier<ValidationContext<E, S>> contextSupplier) {
        this.contextSupplier = contextSupplier;
    }

    @Override
    public ValidatingFunction<S, T> adapt(Validator<E, S> validator, Function<S, T> function) {
        return new Adapter(contextSupplier, validator, function);
    }

    private final class Adapter implements ValidatingFunction<S, T> {

        private final Supplier<ValidationContext<E, S>> contextSupplier;
        private final Validator<E, S> validator;
        private final Function<S, T> function;

        private Adapter(Supplier<ValidationContext<E, S>> contextSupplier, Validator<E, S> validator, Function<S, T> function) {
            this.contextSupplier = contextSupplier;
            this.validator = validator;
            this.function = function;
        }

        @Override
        public T apply(S entity) throws ValidationException {
            final ValidationContext<E, S> context = validate(entity);
            if (context.isValid()) {
                return function.apply(entity);
            } else {
                throw new ValidationException(context.getErrors());
            }
        }

        @Override
        public ValidationContext<E, S> validate(S entity) {
            return validator.validate(entity, contextSupplier.get());
        }
    }
}
