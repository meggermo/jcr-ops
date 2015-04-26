package nl.meg.validation;

import java.util.Map;

public interface Error {

	String getCode();

	String getMessage();

	Map<String, ?> getParameters();

}
