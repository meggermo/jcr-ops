package nl.meg.cr.internal;

import javax.jcr.Value;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class ValueSupport {

    private final Map<Class<?>, Function<Value, ?>> valueFnMap;
    private final JcrSupport jcrSupport;

    @SuppressWarnings("unchecked")
    public <T> Function<Value, T> get(Class<T> type) {
        return (Function<Value, T>) valueFnMap.get(type);
    }

    public ValueSupport(JcrSupport jcrSupport) {
        this.jcrSupport = jcrSupport;
        valueFnMap = new HashMap<>();
        valueFnMap.put(String.class, jcrSupport.wrap(Value::getString));
    }
}
