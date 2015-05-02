package nl.meg.validation;

import aQute.bnd.annotation.ProviderType;

import java.util.Map;

@ProviderType
public interface Error {

	String getCode();

	String getMessage();

	Map<String, ?> getParameters();

}
