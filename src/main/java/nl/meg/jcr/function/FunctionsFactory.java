package nl.meg.jcr.function;

public interface FunctionsFactory {

    NodeFunctions getNodeFunctions();

    NodeTypeFunctions getNodeTypeFunctions();

    PropertyFunctions gePropertyFunctions();

    ValueFunctions getValueFunctions();

}
