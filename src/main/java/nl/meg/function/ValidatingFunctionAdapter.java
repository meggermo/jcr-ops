package nl.meg.function;

import com.google.common.base.Function;
import nl.meg.validation.Validator;

public interface ValidatingFunctionAdapter<E extends Enum<E>, S, T> {

    ValidatingFunction<S, T> adapt(Validator<E, S> validator, Function<S, T> function);
}
