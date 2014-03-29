package nl.meg.jcr.function;

import com.google.common.base.Function;

import javax.jcr.Property;
import javax.jcr.Value;

public interface PropertyFunctions {

    Function<Property, String> getName();

    Function<Property, String> getPath();

    Function<Property, Value> getValue();
}
