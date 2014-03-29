package nl.meg.jcr.function.internal;

import nl.meg.jcr.function.FunctionsFactory;
import nl.meg.jcr.function.*;

public final class FunctionsFactoryImpl implements FunctionsFactory {

    private final NodeFunctions nodeFunctions;
    private final NodeTypeFunctions nodeTypeFunctions;
    private final PropertyFunctions propertyFunctions;
    private final ValueFunctions valueFunctions;

    public FunctionsFactoryImpl(NodeFunctions nodeFunctions, NodeTypeFunctions nodeTypeFunctions, PropertyFunctions propertyFunctions, ValueFunctions valueFunctions) {
        this.nodeFunctions = nodeFunctions;
        this.nodeTypeFunctions = nodeTypeFunctions;
        this.propertyFunctions = propertyFunctions;
        this.valueFunctions = valueFunctions;
    }

    public FunctionsFactoryImpl() {
        this(new NodeFunctionsImpl(), new NodeTypeFunctionsImpl(), new PropertyFunctionsImpl(), new ValueFunctionsImpl());
    }

    @Override
    public NodeFunctions getNodeFunctions() {
        return nodeFunctions;
    }

    @Override
    public NodeTypeFunctions getNodeTypeFunctions() {
        return nodeTypeFunctions;
    }

    @Override
    public PropertyFunctions gePropertyFunctions() {
        return propertyFunctions;
    }

    @Override
    public ValueFunctions getValueFunctions() {
        return valueFunctions;
    }
}
