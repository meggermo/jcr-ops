package nl.meg.cr.internal;

import nl.meg.cr.Property;
import nl.meg.cr.Value;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.RepositoryException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class PropertyImplTest extends AbstractMockitoTest {

    @Mock
    private javax.jcr.Property p;

    @Mock
    private javax.jcr.Value v;

    private Property property;

    @Before
    public void setUp() {
        this.property = new PropertyImpl(p);
    }

    @Test
    public void testGetValue() throws RepositoryException {
        when(p.isMultiple()).thenReturn(false);
        when(p.getValue()).thenReturn(v);
        final List<Value> values = property.getValues().collect(toList());
        assertThat(values.size(), is(1));
        assertThat(values, hasItem(new ValueImpl(v)));
    }

    @Test
    public void testGetValues() throws RepositoryException {
        when(p.isMultiple()).thenReturn(true);
        when(p.getValues()).thenReturn(new javax.jcr.Value[]{v});
        final List<Value> values = property.getValues().collect(toList());
        assertThat(values.size(), is(1));
        assertThat(values, hasItem(new ValueImpl(v)));
    }

    @Test
    public void testEqualsAndHashCode() {
        assertThat(property.equals(null), is(false));
        assertThat(property.equals("test"), is(false));
        assertThat(property.equals(property), is(true));
        assertThat(property.equals(new PropertyImpl(p)), is(true));
        assertThat(property.hashCode(), is(new PropertyImpl(p).hashCode()));
    }
}
