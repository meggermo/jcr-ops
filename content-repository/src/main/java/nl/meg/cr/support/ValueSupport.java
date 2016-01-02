package nl.meg.cr.support;

import javax.jcr.Value;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class ValueSupport {

    private final Map<Class<?>, Function<Value, ?>> valueFnMap;

    @SuppressWarnings("unchecked")
    public <T> Function<Value, T> get(Class<T> type) {
        return (Function<Value, T>) valueFnMap.get(type);
    }

    public ValueSupport() {
        valueFnMap = new HashMap<>();
        valueFnMap.put(String.class, JcrSupport.wrap(Value::getString));
    }
}
