package nl.meg.cr.support;

import javax.jcr.Value;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class ValueSupport {

    @SuppressWarnings("unchecked")
    public static <T> Function<Value, T> get(Class<T> type) {
        return (Function<Value, T>) VALUE_FN_MAP.get(type);
    }

    private static final Map<Class<?>, Function<Value, ?>> VALUE_FN_MAP;

    static {
        VALUE_FN_MAP = new HashMap<>();
        VALUE_FN_MAP.put(String.class, JcrSupport.wrap(Value::getString));
    }
}
