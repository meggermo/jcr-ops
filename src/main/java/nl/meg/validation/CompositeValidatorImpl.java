package nl.meg.validation;

import com.google.common.base.Predicate;

import java.util.Iterator;

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
    public ValidationContext<E, T> validate(T entity, ValidationContext<E, T> context) {
        final Iterator<Validator<E, T>> validatorIterator = validators.iterator();
        while (validatorIterator.hasNext() && continueValidation.apply(context)) {
            context = validatorIterator.next().validate(entity, context);
        }
        return context;
    }

    private static final Predicate HAS_NO_ERRORS = new Predicate<ValidationContext<?, ?>>() {
        @Override
        public boolean apply(ValidationContext<?, ?> context) {
            return context.isValid();
        }
    };
}
