package nl.meg.validation;

import aQute.bnd.annotation.ProviderType;

import java.util.function.Predicate;

@ProviderType
public interface ValidatorBuilder<S> {

    ValidatorBuilder<S> add(Validator<S> validator);

    Validator<S> build(Predicate<ValidationContext> terminationPredicate);
}
