package nl.meg.validation.impl;

import nl.meg.validation.Error;
import nl.meg.validation.ValidationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ValidationContextImpl implements ValidationContext {

	private final Map<String, Error> errorMap;

	private ValidationContextImpl(Map<String, Error> errorMap) {
		this.errorMap = errorMap;
	}

	ValidationContextImpl() {
		this(Collections.<String,Error>emptyMap());
	}

    @Override
	public Map<String, Error> getErrorMap() {
		return Collections.unmodifiableMap(errorMap);
	}

    @Override
	public boolean hasErrors() {
		return !errorMap.isEmpty();
	}

    @Override
	public ValidationContext addError(String validatorId, Error error) {
		final Map<String, Error> newErrorMap = new HashMap<>(errorMap);
		newErrorMap.put(validatorId, error);
		return new ValidationContextImpl(newErrorMap);
	}
	
}
