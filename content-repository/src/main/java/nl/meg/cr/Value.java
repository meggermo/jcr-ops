package nl.meg.cr;

import aQute.bnd.annotation.ProviderType;

import java.math.BigDecimal;
import java.util.Calendar;

@ProviderType
public interface Value {

    boolean getBoolean();

    Calendar getDate();

    BigDecimal getDecimal();

    double getDouble();

    long getLong();

    String getString();

    <E extends Enum<E>> E getEnum(Class<E> enumType);
}
