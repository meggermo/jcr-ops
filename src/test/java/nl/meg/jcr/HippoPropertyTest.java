package nl.meg.jcr;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HippoPropertyTest {


    private HippoProperty hippoProperty;

    @Mock
    private HippoNode hippoNode;

    @Mock
    private Property property;

    @Mock
    private Value value;


    @Before
    public void setUp() {
        this.hippoProperty = new HippoProperty() {
            @Override
            public HippoNode apply(Node node) {
                return hippoNode;
            }

            @Override
            public Property get() {
                return property;
            }
        };
    }

    @Test
    public void testIsMultiple() throws RepositoryException {
        when(property.isMultiple()).thenReturn(true);
        assertThat(hippoProperty.isMultiple(), is(true));
    }

    @Test
    public void testGetValue() throws RepositoryException {
        when(property.getValue()).thenReturn(value);
        assertThat(hippoProperty.getValue().get(), is(value));
    }

    @Test
    public void testGetValue_IsAbsent() throws RepositoryException {
        when(property.isMultiple()).thenReturn(true);
        assertThat(hippoProperty.getValue().isPresent(), is(false));
    }

    @Test
    public void testGetValues() throws RepositoryException {
        when(property.isMultiple()).thenReturn(true);
        when(property.getValues()).thenReturn(new Value[]{value});
        assertThat(hippoProperty.getValues().size(), is(1));
    }

    @Test
    public void testGetValues_NotMultiple() throws RepositoryException {
        when(property.isMultiple()).thenReturn(false);
        assertThat(hippoProperty.getValues().isEmpty(), is(true));
    }

    @Test
    public void testGetValues_EmptyArray() throws RepositoryException {
        when(property.isMultiple()).thenReturn(true);
        when(property.getValues()).thenReturn(new Value[0]);
        assertThat(hippoProperty.getValues().isEmpty(), is(true));
    }

}
