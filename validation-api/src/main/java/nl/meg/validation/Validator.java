package nl.meg.validation;


import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface Validator<T> {

	ValidationContext validate(T subject, ValidationContext context) throws ValidationException;
}
