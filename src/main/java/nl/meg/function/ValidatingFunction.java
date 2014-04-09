package nl.meg.function;

import com.google.common.base.Function;
import nl.meg.validation.ValidationContext;

public interface ValidatingFunction<S, T> extends Function<S, T> {

    ValidationContext<? extends Enum<?>, S> validate(S entity);
}
