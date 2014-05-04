package nl.meg.validation;


import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

public abstract class PredicateBasedValidatorImpl<E, T> implements Validator<E, T> {

    private final Predicate<T> p;

    public PredicateBasedValidatorImpl(Predicate<T> p) {
        this.p = p;
    }

    @Override
    public final ValidationContext<E, T> validate(T entity, ValidationContext<E, T> context) {
        if (p.test(entity)) {
            return context;
        } else {
            return context.addError(getError(), getContextMap(entity));
        }
    }

    protected abstract E getError();

    protected Map<String, ?> getContextMap(T entity) {
        return Collections.emptyMap();
    }
}
