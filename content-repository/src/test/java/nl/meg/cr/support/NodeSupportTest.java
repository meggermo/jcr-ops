package nl.meg.cr.support;

import nl.meg.cr.internal.AbstractMockitoTest;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class NodeSupportTest extends AbstractMockitoTest {

    @Mock
    private Node node;
    @Mock
    private Value value;
    @Mock
    private Property property;

    @Test
    public void testSetValue_bound_to_value() throws RepositoryException {
        final String propName = "property";
        when(node.setProperty(propName, value)).thenReturn(property);

        final BiFunction<Node, String, Property> v = NodeSupport.setValue(value);
        assertThat(v.apply(node, propName), is(property));
        assertThat(v.apply(node, "someOther" + propName), is(nullValue()));
    }

    @Test
    public void testSetValue_bound_to_node() throws RepositoryException {
        final String propName = "property";
        when(node.setProperty(propName, value)).thenReturn(property);

        final BiFunction<String, Value, Property> v = NodeSupport.setValue(node);
        assertThat(v.apply(propName, value), is(property));
        assertThat(v.apply("someOther" + propName, value), is(nullValue()));
    }

    @Test
    public void testSetValue_bound_to_property() throws RepositoryException {
        final String propName = "property";
        when(node.setProperty(propName, value)).thenReturn(property);

        final BiFunction<Node, Value, Property> v = NodeSupport.setValue(propName);
        assertThat(v.apply(node, value), is(property));
    }

    @Test
    public void testSetValues_bound_to_value() throws RepositoryException {
        final String propName = "property";
        final List<Value> valueList = Collections.singletonList(value);
        final Value[] valueArray = valueList.toArray(new Value[valueList.size()]);
        when(node.setProperty(propName, valueArray)).thenReturn(property);

        final BiFunction<Node, String, Property> v = NodeSupport.setValues(valueList);
        assertThat(v.apply(node, propName), is(property));
        assertThat(v.apply(node, "someOther" + propName), is(nullValue()));
    }

    @Test
    public void testSetValues_bound_to_node() throws RepositoryException {
        final String propName = "property";
        final List<Value> valueList = Collections.singletonList(value);
        final Value[] valueArray = valueList.toArray(new Value[valueList.size()]);
        when(node.setProperty(propName, valueArray)).thenReturn(property);

        final BiFunction<String, List<Value>, Property> v = NodeSupport.setValues(node);
        assertThat(v.apply(propName, valueList), is(property));
        assertThat(v.apply("someOther" + propName, valueList), is(nullValue()));
    }

    @Test
    public void testSetValues_bound_to_property() throws RepositoryException {
        final String propName = "property";
        final List<Value> valueList = Collections.singletonList(value);
        final Value[] valueArray = valueList.toArray(new Value[valueList.size()]);
        when(node.setProperty(propName, valueArray)).thenReturn(property);

        final BiFunction<Node, List<Value>, Property> v = NodeSupport.setValues(propName);
        assertThat(v.apply(node, valueList), is(property));
    }
}
