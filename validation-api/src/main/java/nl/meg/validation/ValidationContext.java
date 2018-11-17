package nl.meg.validation;


import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ValidationContext {

	boolean hasErrors();

	ValidationContext addError(String validatorId, Error error);

    Map<String,Error> getErrorMap();
}
