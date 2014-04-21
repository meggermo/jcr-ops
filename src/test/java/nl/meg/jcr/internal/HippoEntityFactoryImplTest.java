package nl.meg.jcr.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoEntityFactory;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.HippoValue;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertThat;

public class HippoEntityFactoryImplTest extends AbstractMockitoTest {

    private HippoEntityFactory factory;

    @Before
    public void setUp() {
        this.factory = new HippoEntityFactoryImpl();
    }

    @Test
    public void testValue() {
        assertThat(factory.value(null), isA(HippoValue.class));
    }

    @Test
    public void testProperty() {
        assertThat(factory.property(null), isA(HippoProperty.class));
    }

    @Test
    public void testNode() {
        assertThat(factory.node(null), isA(HippoNode.class));
    }
}
