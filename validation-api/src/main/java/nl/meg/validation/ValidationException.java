package nl.meg.validation;

import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    private final Map<String, Error> errorMap;

    public ValidationException(Map<String, Error> errorMap, String message, Throwable cause) {
		super(message, cause);
        this.errorMap = errorMap;
	}

    public ValidationException(Map<String, Error> errorMap, String message) {
		super(message);
        this.errorMap = errorMap;
	}

    public ValidationException(Map<String, Error> errorMap, Throwable cause) {
		super(cause);
        this.errorMap = errorMap;
	}

	public Map<String, Error> getErrorMap() {
        return errorMap;
    }

}
