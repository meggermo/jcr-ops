package nl.meg.jcr.internal;

import nl.meg.jcr.HippoValue;
import nl.meg.jcr.RuntimeRepositoryException;

import javax.jcr.Binary;
import javax.jcr.Value;
import java.math.BigDecimal;
import java.util.Calendar;

import static nl.meg.function.FunctionSupport.relax;


final class HippoValueImpl implements HippoValue {

    private final Value value;

    HippoValueImpl(Value value) {
        this.value = value;
    }

    @Override
    public Binary getBinary() {
        return relax(Value::getBinary, get(), RuntimeRepositoryException::new);
    }

    @Override
    public boolean getBoolean() {
        return relax(Value::getBoolean, get(), RuntimeRepositoryException::new);
    }

    @Override
    public Calendar getDate() {
        return relax(Value::getDate, get(), RuntimeRepositoryException::new);
    }

    @Override
    public BigDecimal getDecimal() {
        return relax(Value::getDecimal, get(), RuntimeRepositoryException::new);
    }

    @Override
    public Double getDouble() {
        return relax(Value::getDouble, get(), RuntimeRepositoryException::new);
    }

    @Override
    public Long getLong() {
        return relax(Value::getLong, get(), RuntimeRepositoryException::new);
    }

    @Override
    public String getString() {
        return relax(Value::getString, get(), RuntimeRepositoryException::new);
    }

    @Override
    public <E extends Enum<E>> E getEnum(Class<E> enumType) {
        return Enum.valueOf(enumType, getString());
    }

    @Override
    public Value get() {
        return value;
    }
}
