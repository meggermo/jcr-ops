package nl.meg.jcr.function;

import com.google.common.base.Function;

import javax.jcr.Value;
import java.util.Calendar;

public interface ValueFunctions {

    Function<Value, String> getString();

    Function<Value, Boolean> getBoolean();

    Function<Value, Long> getLong();

    Function<Value, Calendar> getDate();
}
