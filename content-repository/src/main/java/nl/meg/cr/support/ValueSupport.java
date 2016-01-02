package nl.meg.cr.support;

import nl.meg.cr.support.JcrSupport;

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

    public ValueSupport(JcrSupport jcrSupport) {
        valueFnMap = new HashMap<>();
        valueFnMap.put(String.class, jcrSupport.wrap(Value::getString));
    }
}
