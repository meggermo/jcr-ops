package nl.meg.validation;


import java.util.Iterator;
import java.util.function.Predicate;

public final class CompositeValidatorImpl<E, T> implements Validator<E, T> {

    private final Iterable<Validator<E, T>> validators;

    private final Predicate<ValidationContext<E, T>> continueValidation;

    public CompositeValidatorImpl(Iterable<Validator<E, T>> validators, Predicate<ValidationContext<E, T>> continueValidation) {
        this.validators = validators;
        this.continueValidation = continueValidation;
    }

    public CompositeValidatorImpl(Iterable<Validator<E, T>> validators) {
        this(validators, ValidationContext::isValid);
    }

    @Override
    public ValidationContext<E, T> validate(T entity, ValidationContext<E, T> context) {
        final Iterator<Validator<E, T>> validatorIterator = validators.iterator();
        while (validatorIterator.hasNext() && continueValidation.test(context)) {
            context = validatorIterator.next().validate(entity, context);
        }
        return context;
    }

}
