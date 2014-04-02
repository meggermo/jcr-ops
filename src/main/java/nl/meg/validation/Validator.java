package nl.meg.validation;

public interface Validator<E extends Enum<E>, T> {

    ValidationContext<E, T> validate(T entity, ValidationContext<E, T> context);
}
