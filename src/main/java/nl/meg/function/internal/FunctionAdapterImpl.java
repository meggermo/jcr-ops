package nl.meg.function.internal;

import nl.meg.function.FunctionAdapter;
import nl.meg.function.ValidationException;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;

import java.util.function.Function;
import java.util.function.Supplier;

final class FunctionAdapterImpl<E extends Enum<E>, S, T> implements FunctionAdapter<E, S, T> {

    private final Supplier<ValidationContext<E, S>> contextSupplier;

    public FunctionAdapterImpl(Supplier<ValidationContext<E, S>> contextSupplier) {
        this.contextSupplier = contextSupplier;
    }

    @Override
    public Function<S, T> preValidate(Validator<E, S> validator, Function<S, T> function) {
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
        public T apply(S entity) throws ValidationException {
            final ValidationContext<E, S> context = validate(entity);
            if (context.isValid()) {
                return function.apply(entity);
            } else {
                throw new ValidationException(context.getErrors());
            }
        }

        private ValidationContext<E, S> validate(S entity) {
            return validator.validate(entity, contextSupplier.get());
        }
    }
}
