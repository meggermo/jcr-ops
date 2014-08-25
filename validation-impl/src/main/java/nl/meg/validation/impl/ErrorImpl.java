package nl.meg.validation.impl;

import java.util.Collections;
import java.util.Map;

public final class ErrorImpl implements nl.meg.validation.Error {

	private final String code;
	private final String message;
	private final Map<String,?> parameters;
	
	public ErrorImpl(String code, String message, Map<String, ?> parameters) {
		this.code = code;
		this.message = message;
		this.parameters = parameters;
	}

	public ErrorImpl(String code, String message) {
		this(code, message, Collections.<String,Object>emptyMap());
	}

    @Override
	public String getCode() {
		return code;
	}

    @Override
	public String getMessage() {
		return message;
	}

    @Override
	public Map<String, ?> getParameters() {
		return parameters;
	}

}
