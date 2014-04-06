package nl.meg.jcr.predicate.internal;

import nl.meg.jcr.function.FunctionsFactory;
import nl.meg.jcr.function.internal.FunctionsFactoryImpl;
import nl.meg.jcr.predicate.*;

public final class PredicatesFactoryImpl implements PredicatesFactory {

    private final NodePredicates nodePredicates;
    private final NodeTypePredicates nodeTypePredicates;
    private final PropertyPredicates propertyPredicates;
    private final ValuePredicates valuePredicates;

    public PredicatesFactoryImpl(NodePredicates nodePredicates, NodeTypePredicates nodeTypePredicates, PropertyPredicates propertyPredicates, ValuePredicates valuePredicates) {
        this.nodePredicates = nodePredicates;
        this.nodeTypePredicates = nodeTypePredicates;
        this.propertyPredicates = propertyPredicates;
        this.valuePredicates = valuePredicates;
    }

    public PredicatesFactoryImpl(FunctionsFactory functionsFactory) {
        this(new NodePredicatesImpl(functionsFactory.getNodeFunctions()),
                new NodeTypePredicatesImpl(functionsFactory.getNodeTypeFunctions()),
                new PropertyPredicatesImpl(functionsFactory.gePropertyFunctions()),
                new ValuePredicatesImpl(functionsFactory.getValueFunctions()));
    }

    public PredicatesFactoryImpl() {
        this(new FunctionsFactoryImpl());
    }

    @Override
    public NodePredicates getNodePredicates() {
        return nodePredicates;
    }

    @Override
    public NodeTypePredicates getNodeTypePredicates() {
        return nodeTypePredicates;
    }

    @Override
    public PropertyPredicates gePropertyPredicates() {
        return propertyPredicates;
    }

    @Override
    public ValuePredicates getValuePredicates() {
        return valuePredicates;
    }

}
