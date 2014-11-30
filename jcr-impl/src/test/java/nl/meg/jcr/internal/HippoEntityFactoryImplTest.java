package nl.meg.jcr.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoEntityFactory;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.HippoValue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Value;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertThat;

public class HippoEntityFactoryImplTest extends AbstractMockitoTest {

    private HippoEntityFactory factory;

    @Mock
    private Node node;

    @Mock
    private Value value;

    @Mock
    private Property property;

    @Before
    public void setUp() {
        this.factory = new HippoEntityFactoryImpl();
    }

    @Test
    public void testValue() {
        assertThat(factory.value(value), isA(HippoValue.class));
    }

    @Test
    public void testProperty() {
        assertThat(factory.property(property), isA(HippoProperty.class));
    }

    @Test
    public void testNode() {
        assertThat(factory.node(node), isA(HippoNode.class));
    }
}
