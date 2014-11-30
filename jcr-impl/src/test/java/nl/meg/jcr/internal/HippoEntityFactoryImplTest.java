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

    @Test(expected = AssertionError.class)
    public void testValue_throws_assertion_error_on_null_arg() {
        factory.value(null);
    }

    @Test(expected = AssertionError.class)
    public void testProperty_throws_assertion_error_on_null_arg() {
        factory.property(null);
    }

    @Test(expected = AssertionError.class)
    public void testNode_throws_assertion_error_on_null_arg() {
        factory.node(null);
    }

    @Test(expected = AssertionError.class)
    public void testVersion_throws_assertion_error_on_null_arg() {
        factory.version(null);
    }

    @Test(expected = AssertionError.class)
    public void testVersionHistory_throws_assertion_error_on_null_arg() {
        factory.versionHistory(null);
    }
}
