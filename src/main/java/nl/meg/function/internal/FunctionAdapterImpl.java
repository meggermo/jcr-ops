package nl.meg.function.internal;

import nl.meg.function.FunctionAdapter;
import nl.meg.function.ValidationException;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;

import java.util.function.Function;

final class FunctionAdapterImpl<E extends Enum<E>, S, T> implements FunctionAdapter<E, S, T> {

    @Override
    public Function<S, T> preValidate(Validator<E, S> validator, Function<S, T> function) {
        return entity -> {
            final ValidationContext<E, S> context = validator.validate(entity, new ValidationContext<E, S>());
            if (context.isValid()) {
                return function.apply(entity);
            } else {
                throw new ValidationException(context.getErrors());
            }
        };
    }

}
