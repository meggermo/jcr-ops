package nl.meg.function;

import nl.meg.validation.ValidationContext;

import java.util.function.Function;

public interface ValidatingFunction<S, T> extends Function<S, T> {

    ValidationContext<? extends Enum<?>, S> validate(S entity);
}
