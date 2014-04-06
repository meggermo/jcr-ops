package nl.meg.jcr.predicate;

public interface PredicatesFactory {

    NodePredicates getNodePredicates();

    NodeTypePredicates getNodeTypePredicates();

    PropertyPredicates gePropertyPredicates();

    ValuePredicates getValuePredicates();

}
