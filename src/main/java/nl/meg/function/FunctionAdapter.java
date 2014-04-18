package nl.meg.function;

import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;

import java.util.function.Function;

public final class FunctionAdapter {

    /**
     * Returns a function that will pre-validate it's argument before executing the given function.
     * If validation fails the returned function will throw a {@link nl.meg.function.ValidationException}.
     *
     * @param validator a pre-validator
     * @param function  function to call if validation succeeds
     * @return function that pre-validates the input
     */
    public static <E extends Enum<E>, S, T> Function<S, T> preValidate(Validator<E, S> validator, Function<S, T> function) {
        return entity -> {
            final ValidationContext<E, S> context = validator.validate(entity, new ValidationContext<E, S>());
            if (context.isValid()) {
                return function.apply(entity);
            } else {
                throw new ValidationException(context.getErrors());
            }
        };
    }

    /**
     * Returns a function that will post-validate the result after executing the given function.
     * If validation fails the returned function will throw a {@link nl.meg.function.ValidationException}.
     *
     * @param validator a post-validator
     * @param function  function to call before validation
     * @return function that post-validates the result
     */
    public static <E extends Enum<E>, S, T> Function<S, T> postValidate(Validator<E, T> validator, Function<S, T> function) {
        return entity -> {
            final T result = function.apply(entity);
            final ValidationContext<E, T> context = validator.validate(result, new ValidationContext<E, T>());
            if (context.isValid()) {
                return result;
            } else {
                throw new ValidationException(context.getErrors());
            }
        };
    }

    public static <T, R, E extends Exception, RE extends RuntimeException> R relax(EFunction<? super T, ? extends R, E> f, T input, Function<E, ? extends RE> g) {
        try {
            return f.apply(input);
        } catch (Exception e) {
            throw g.apply((E) e);
        }
    }

}
