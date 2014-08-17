package nl.meg.validation;

public interface Validator<T> {

	ValidationContext validate(T subject, ValidationContext context) throws ValidationException;
}
