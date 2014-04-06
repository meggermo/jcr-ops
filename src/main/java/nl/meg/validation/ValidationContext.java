package nl.meg.validation;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public interface ValidationContext<E extends Enum<E>, T> {

    boolean isValid();

    EnumMap<E, List<Map<String, ?>>> getErrors();

    ValidationContext<E, T> addError(E error, Map<String, ?> contextParameterMap);
}
