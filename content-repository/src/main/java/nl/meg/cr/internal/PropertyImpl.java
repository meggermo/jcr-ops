package nl.meg.cr.internal;

import nl.meg.cr.Property;
import nl.meg.cr.RepositoryException;
import nl.meg.cr.Value;

import java.util.stream.Stream;

import static nl.meg.cr.internal.ExceptionSupport.tryInvoke;

final class PropertyImpl implements Property {

    private final javax.jcr.Property delegate;

    PropertyImpl(javax.jcr.Property delegate) {
        this.delegate = delegate;
    }

    @Override
    public Stream<Value> getValues() {
        return streamOfJcrValues().map(ValueImpl::new);
    }

    private Stream<javax.jcr.Value> streamOfJcrValues() {
        if (isMultiple()) {
            return Stream.of(getJcrValues());
        } else {
            return Stream.of(getJcrValue());
        }
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertyImpl property = (PropertyImpl) o;

        return delegate.equals(property.delegate);

    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    private boolean isMultiple() {
        return tryGet(javax.jcr.Property::isMultiple, boolean.class);
    }

    private javax.jcr.Value getJcrValue() {
        return tryGet(javax.jcr.Property::getValue, javax.jcr.Value.class);
    }

    private javax.jcr.Value[] getJcrValues() {
        return tryGet(javax.jcr.Property::getValues, javax.jcr.Value[].class);
    }

    private <Y> Y tryGet(ExceptionSupport.EFunction<javax.jcr.Property, Y, javax.jcr.RepositoryException> f, Class<Y> type) {
        return tryInvoke(f, delegate, RepositoryException::new);
    }

}
