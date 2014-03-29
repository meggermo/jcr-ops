package nl.meg.jcr.predicate.internal;

import nl.meg.jcr.predicate.PredicatesFactory;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PredicatesFactoryImplTest {

    private final PredicatesFactory predicatesFactory = new PredicatesFactoryImpl();

    @Test
    public void testGetNodePredicates() {
        assertThat(NodePredicatesImpl.class.isInstance(predicatesFactory.getNodePredicates()),is(true));
    }

    @Test
    public void testGetNodeTypePredicates() {
        assertThat(NodeTypePredicatesImpl.class.isInstance(predicatesFactory.getNodeTypePredicates()),is(true));

    }

    @Test
    public void testGePropertyPredicates() {
        assertThat(PropertyPredicatesImpl.class.isInstance(predicatesFactory.gePropertyPredicates()),is(true));
    }

    @Test
    public void testGetValuePredicates() {
        assertThat(ValuePredicatesImpl.class.isInstance(predicatesFactory.getValuePredicates()),is(true));
    }
}
