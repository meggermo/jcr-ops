package nl.meg.jcr.predicate;

import nl.meg.jcr.predicate.NodePredicates;
import nl.meg.jcr.predicate.NodeTypePredicates;
import nl.meg.jcr.predicate.PropertyPredicates;
import nl.meg.jcr.predicate.ValuePredicates;

public interface PredicatesFactory {

    NodePredicates getNodePredicates();

    NodeTypePredicates getNodeTypePredicates();

    PropertyPredicates gePropertyPredicates();

    ValuePredicates getValuePredicates();

}
