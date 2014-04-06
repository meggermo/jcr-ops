package nl.meg.validation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class ValidationContextImpl<E extends Enum<E>, T> implements ValidationContext<E, T> {

    private final EnumMap<E, List<Map<String, ?>>> errors;

    public ValidationContextImpl(Class<E> errorCodeType) {
        errors = new EnumMap<>(errorCodeType);
    }

    @Override
    public boolean isValid() {
        return errors.isEmpty();
    }

    @Override
    public EnumMap<E, List<Map<String, ?>>> getErrors() {
        return errors;
    }

    @Override
    public ValidationContext<E, T> addError(E error, Map<String, ?> contextParameterMap) {
        if (!errors.containsKey(error)) {
            errors.put(error, new ArrayList<Map<String, ?>>());
        }
        errors.get(error).add(contextParameterMap);
        return this;
    }
}
