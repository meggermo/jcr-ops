package nl.meg.validation;


import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface Error {

	String getCode();

	String getMessage();

	Map<String, ?> getParameters();

}
