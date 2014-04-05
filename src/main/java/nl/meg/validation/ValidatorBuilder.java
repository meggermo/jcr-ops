package nl.meg.validation;

import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;

public class ValidatorBuilder<E extends Enum<E>, T> {

    private final List<Validator<E, T>> validators;
    private final Predicate<ValidationContext<E, T>> continuePredicate;

    public static <X extends Enum<X>, Y> ValidatorBuilder<X,Y> builder(Predicate<ValidationContext<X, Y>> continuePredicate) {
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
