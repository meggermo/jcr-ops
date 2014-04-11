package nl.meg.function;

import nl.meg.validation.Validator;

import java.util.function.Function;

public interface ValidatingFunctionAdapter<E extends Enum<E>, S, T> {

    ValidatingFunction<S, T> adapt(Validator<E, S> validator, Function<S, T> function);
}
