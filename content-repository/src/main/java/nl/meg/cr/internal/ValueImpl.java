package nl.meg.cr.internal;

import nl.meg.cr.RepositoryException;
import nl.meg.cr.Value;
import nl.meg.cr.internal.ExceptionSupport.EFunction;

import java.math.BigDecimal;
import java.util.Calendar;

import static nl.meg.cr.internal.ExceptionSupport.tryInvoke;

final class ValueImpl implements Value {

    private final javax.jcr.Value delegate;

    ValueImpl(javax.jcr.Value delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean getBoolean() {
        return tryGet(javax.jcr.Value::getBoolean, boolean.class);
    }

    @Override
    public Calendar getDate() {
        return tryGet(javax.jcr.Value::getDate, Calendar.class);
    }

    @Override
    public BigDecimal getDecimal() {
        return tryGet(javax.jcr.Value::getDecimal, BigDecimal.class);
    }

    @Override
    public double getDouble() {
        return tryGet(javax.jcr.Value::getDouble, double.class);
    }

    @Override
    public long getLong() {
        return tryGet(javax.jcr.Value::getLong, long.class);
    }

    @Override
    public String getString() {
        return tryGet(javax.jcr.Value::getString, String.class);
    }

    @Override
    public <E extends Enum<E>> E getEnum(Class<E> enumType) {
        return Enum.valueOf(enumType, getString());
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValueImpl value = (ValueImpl) o;

        return delegate.equals(value.delegate);

    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    private <Y> Y tryGet(EFunction<javax.jcr.Value, Y, javax.jcr.RepositoryException> f, Class<Y> type) {
        return tryInvoke(f, delegate, RepositoryException::new);
    }

}
