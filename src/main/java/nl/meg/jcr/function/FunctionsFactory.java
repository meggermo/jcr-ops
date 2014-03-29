package nl.meg.jcr.function;

import nl.meg.jcr.function.NodeFunctions;
import nl.meg.jcr.function.NodeTypeFunctions;
import nl.meg.jcr.function.PropertyFunctions;
import nl.meg.jcr.function.ValueFunctions;

public interface FunctionsFactory {

    NodeFunctions getNodeFunctions();

    NodeTypeFunctions getNodeTypeFunctions();

    PropertyFunctions gePropertyFunctions();

    ValueFunctions getValueFunctions();

}
