package nl.meg.cr.support;

import javax.jcr.Value;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class ValueSupport {

    @SuppressWarnings("unchecked")
    public static <T> Function<Value, T> valueToType(Class<T> type) {
        return (Function<Value, T>) VAL_TO_TYPE_MAP.get(type);
    }

    private static final Map<Class<?>, Function<Value, ?>> VAL_TO_TYPE_MAP;

    static {
        VAL_TO_TYPE_MAP = new HashMap<>();
        VAL_TO_TYPE_MAP.put(String.class, JcrSupport.wrap(Value::getString));
    }
}
