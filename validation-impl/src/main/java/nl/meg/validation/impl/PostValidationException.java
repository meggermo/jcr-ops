package nl.meg.validation.impl;

import nl.meg.validation.Error;
import nl.meg.validation.ValidationException;

import java.util.Map;

public final class PostValidationException extends ValidationException {

	private static final long serialVersionUID = 1L;
	
	public PostValidationException(Map<String, Error> errorMap) {
		super(errorMap, "postcondition violated");
	}

}
