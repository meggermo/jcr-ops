package nl.meg.jcr.traversal.internal;

import nl.meg.jcr.traversal.TraversalFactory;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TraversalFactoryImplTest {

    private TraversalFactory factory;

    @Before
    public void setUp() {
        this.factory = new TraversalFactoryImpl();
    }

    @Test
    public void testAncestorTraverser() {
        assertThat(AncestorTraverserImpl.class.isInstance(factory.ancestorTraverser()), is(true));
    }

    @Test
    public void testDescendantTraverser() {
        assertThat(DescendantTraverserImpl.class.isInstance(factory.descendantTraverser()), is(true));
    }

    @Test
    public void testWhileIterables() {
        assertThat(WhileIterablesImpl.class.isInstance(factory.whileIterables()),is(true));
    }
}
