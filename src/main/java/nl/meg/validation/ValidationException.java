package nl.meg.validation;

import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {

    private final Map<?, List<Map<String, ?>>> errors;

    public ValidationException(Map<?, List<Map<String, ?>>> errors) {
        this.errors = errors;
    }

    public Map<?, List<Map<String, ?>>> getErrors() {
        return errors;
    }
}
