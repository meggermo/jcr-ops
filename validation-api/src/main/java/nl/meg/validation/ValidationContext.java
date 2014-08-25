package nl.meg.validation;

import java.util.Map;

public interface ValidationContext {

	boolean hasErrors();
	
	ValidationContext addError(String validatorId, Error error);

    Map<String,Error> getErrorMap();
}
