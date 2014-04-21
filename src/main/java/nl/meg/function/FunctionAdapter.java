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
            final ValidationContext<E, S> context = validator.validate(entity, new ValidationContext<>());
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
            final ValidationContext<E, T> context = validator.validate(result, new ValidationContext<>());
            if (context.isValid()) {
                return result;
            } else {
                throw new ValidationException(context.getErrors());
            }
        };
    }

    /**
     * Calls a function that throws a checked {@link Exception} of type E and returns the result if no exception occurs. If calling the function results in an exception
     * then the given function g is used to throw an unchecked exception instead.
     *
     * @param f     a function that throws a checked exception
     * @param input value for the function
     * @param g     transformation function E -> RE
     * @param <T>   type of input
     * @param <R>   type of output
     * @param <E>   type of checked exception
     * @param <RE>  type of unchecked exception
     * @return result of f applied to input
     */
    @SuppressWarnings("unchecked")
    public static <T, R, E extends Exception, F extends EFunction<? super T, ? extends R, E>, RE extends RuntimeException> R relax(F f, T input, Function<E, ? extends RE> g) {
        try {
            return f.apply(input);
        } catch (Exception e) {
            // Safe cast, because f can only throw an instance of (a subclass of) E
            throw g.apply((E) e);
        }
    }

}
