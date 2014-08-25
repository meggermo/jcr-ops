package nl.meg.validation;

import java.util.Map;

public interface Error {

	public String getCode();

	public String getMessage();

	public Map<String, ?> getParameters();

}
