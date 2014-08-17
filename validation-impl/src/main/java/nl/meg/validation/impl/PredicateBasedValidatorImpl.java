package nl.meg.validation.impl;


import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;

import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

public abstract class PredicateBasedValidatorImpl<T> implements Validator<T> {

    private final Predicate<T> p;

    public PredicateBasedValidatorImpl(Predicate<T> p) {
        this.p = p;
    }

    @Override
    public final ValidationContext validate(T entity, ValidationContext context) {
        if (p.test(entity)) {
            return context;
        } else {
            return context.addError(getValidatorId(), new ErrorImpl(getCode(), getMessage(), getContextMap(entity)));
        }
    }

    protected abstract String getCode();

    protected abstract String getMessage();

    protected String getValidatorId() {
        return getClass().getName();
    }

    protected Map<String, ?> getContextMap(T entity) {
        return Collections.emptyMap();
    }
}
