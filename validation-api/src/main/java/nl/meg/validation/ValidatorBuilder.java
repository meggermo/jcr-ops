package nl.meg.validation;

import java.util.function.Predicate;

public interface ValidatorBuilder<S> {

    ValidatorBuilder<S> add(Validator<S> validator);

    Validator<S> build(Predicate<ValidationContext> terminationPredicate);
}
