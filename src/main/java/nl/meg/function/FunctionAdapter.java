package nl.meg.function;

import nl.meg.validation.Validator;

import java.util.function.Function;

public interface FunctionAdapter<E extends Enum<E>, S, T> {

    Function<S, T> preValidate(Validator<E, S> validator, Function<S, T> function);

    Function<S, T> postValidate(Validator<E, T> validator, Function<S, T> function);

}
