package nl.meg.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ValidatorBuilder<E, T> {

    private final List<Validator<E, T>> validators;
    private final Predicate<ValidationContext<E, T>> continuePredicate;

    public static <X, Y> ValidatorBuilder<X, Y> builder(Predicate<ValidationContext<X, Y>> continuePredicate) {
        return new ValidatorBuilder<>(continuePredicate);
    }

    public ValidatorBuilder(Predicate<ValidationContext<E, T>> continuePredicate) {
        this.validators = new ArrayList<>();
        this.continuePredicate = continuePredicate;
    }

    public ValidatorBuilder<E, T> add(Validator<E, T> validator) {
        this.validators.add(validator);
        return this;
    }

    public Validator<E, T> build() {
        return new CompositeValidatorImpl<>(validators, continuePredicate);
    }
}
