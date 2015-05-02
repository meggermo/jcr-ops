package nl.meg.validation;

import aQute.bnd.annotation.ConsumerType;

@ConsumerType
public interface Validator<T> {

	ValidationContext validate(T subject, ValidationContext context) throws ValidationException;
}
