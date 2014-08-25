package nl.meg.validation.impl;

import nl.meg.validation.Error;
import nl.meg.validation.ValidationException;

import java.util.Map;

public final class PreValidationException extends ValidationException {

	private static final long serialVersionUID = 1L;

	PreValidationException(Map<String, Error> errorMap) {
		super(errorMap, "precondition violated");
	}

}
