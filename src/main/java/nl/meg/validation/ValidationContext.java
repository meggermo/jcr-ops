package nl.meg.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationContext<E extends Enum<E>, T> {

    private final Map<E, List<Map<String, ?>>> errors = new HashMap<>();

    public boolean isValid() {
        return errors.isEmpty();
    }

    public Map<E, List<Map<String, ?>>> getErrors() {
        return errors;
    }

    public ValidationContext<E, T> addError(E error, Map<String, ?> contextParameterMap) {
        if (!errors.containsKey(error)) {
            errors.put(error, new ArrayList<Map<String, ?>>());
        }
        errors.get(error).add(contextParameterMap);
        return this;
    }
}
