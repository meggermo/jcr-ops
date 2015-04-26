package nl.meg.jcr;

import javax.jcr.Binary;
import javax.jcr.Value;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.function.Supplier;

public interface HippoValue extends Supplier<Value> {

    Binary getBinary();

    boolean getBoolean();

    Calendar getDate();

    BigDecimal getDecimal();

    Double getDouble();

    Long getLong();

    String getString();

    <E extends Enum<E>> E getEnum(Class<E> enumType);
}
