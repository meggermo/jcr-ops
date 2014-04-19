package nl.meg.jcr;

import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.Binary;
import javax.jcr.Value;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.function.Supplier;

import static nl.meg.function.FunctionAdapter.relax;

public interface HippoValue extends Supplier<Value> {

    default Binary getBinary() {
        return relax(Value::getBinary, get(), RuntimeRepositoryException::new);
    }

    default Boolean getBoolean() {
        return relax(Value::getBoolean, get(), RuntimeRepositoryException::new);
    }

    default Calendar getDate() {
        return relax(Value::getDate, get(), RuntimeRepositoryException::new);
    }

    default BigDecimal getDecimal() {
        return relax(Value::getDecimal, get(), RuntimeRepositoryException::new);
    }

    default Double getDouble() {
        return relax(Value::getDouble, get(), RuntimeRepositoryException::new);
    }

    default Long getLong() {
        return relax(Value::getLong, get(), RuntimeRepositoryException::new);
    }

    default String getString() {
        return relax(Value::getString, get(), RuntimeRepositoryException::new);
    }
}
