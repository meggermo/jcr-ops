package nl.meg.validation;

import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {

    private final Map<? extends Enum<?>, List<Map<String, ?>>> errors;

    public ValidationException(Map<? extends Enum<?>, List<Map<String, ?>>> errors) {
        this.errors = errors;
    }

    public Map<? extends Enum, List<Map<String, ?>>> getErrors() {
        return errors;
    }
}
