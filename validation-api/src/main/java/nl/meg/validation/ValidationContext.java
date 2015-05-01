package nl.meg.validation;

import aQute.bnd.annotation.ProviderType;

import java.util.Map;

@ProviderType
public interface ValidationContext {

	boolean hasErrors();

	ValidationContext addError(String validatorId, Error error);

    Map<String,Error> getErrorMap();
}
