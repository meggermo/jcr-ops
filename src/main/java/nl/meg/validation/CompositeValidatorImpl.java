package nl.meg.validation;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import static com.google.common.collect.FluentIterable.from;

public final class CompositeValidatorImpl<E extends Enum<E>, T> implements Validator<E, T> {

    private final Iterable<Validator<E, T>> validators;

    private final Predicate<ValidationContext<E, T>> continueValidation;

    public CompositeValidatorImpl(Iterable<Validator<E, T>> validators, Predicate<ValidationContext<E, T>> continueValidation) {
        this.validators = validators;
        this.continueValidation = continueValidation;
    }

    public CompositeValidatorImpl(Iterable<Validator<E, T>> validators) {
        this(validators, HAS_NO_ERRORS);
    }

    @Override
    public ValidationContext<E, T> validate(final T entity, final ValidationContext<E, T> context) {
        final Function<Validator<E, T>, ValidationContext<E, T>> contextFunction = new Function<Validator<E, T>, ValidationContext<E, T>>() {
            @Override
            public ValidationContext<E, T> apply(Validator<E, T> input) {
                if (continueValidation.apply(context)) {
                    return input.validate(entity, context);
                } else {
                    return context;
                }
            }
        };
        return from(validators).transform(contextFunction).last().get();
    }

    private static final Predicate HAS_NO_ERRORS = new Predicate<ValidationContext<?, ?>>() {
        @Override
        public boolean apply(ValidationContext<?, ?> context) {
            return context.isValid();
        }
    };
}
