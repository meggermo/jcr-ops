package nl.meg.validation;

public interface Validator<E, T> {

    ValidationContext<E, T> validate(T entity, ValidationContext<E, T> context);
}
