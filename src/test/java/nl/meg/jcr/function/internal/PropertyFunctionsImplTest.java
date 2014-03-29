package nl.meg.jcr.function.internal;

import nl.meg.jcr.exception.RuntimeRepositoryException;
import nl.meg.jcr.function.PropertyFunctions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PropertyFunctionsImplTest {

    private final PropertyFunctions propertyFunctions = new PropertyFunctionsImpl();

    @Mock
    private Property p;

    @Mock
    private Value v;

    @Mock
    private RepositoryException e;

    @Before
    public void setUp() {

    }

    @Test
    public void testGetName() throws RepositoryException {
        when(p.getName()).thenReturn("name");
        assertThat(propertyFunctions.getName().apply(p), is("name"));
    }

    @Test
    public void testGetPath() throws RepositoryException {
        when(p.getPath()).thenReturn("path");
        assertThat(propertyFunctions.getPath().apply(p), is("path"));
    }

    @Test
    public void testGetValue() throws RepositoryException {
        when(p.getValue()).thenReturn(v);
        assertThat(propertyFunctions.getValue().apply(p), is(v));
    }

    @Test
    public void testThrowing() throws RepositoryException {
        final Throwable t = e;
        try {
            when(p.getName()).thenThrow(e);
            propertyFunctions.getName().apply(p);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(p.getPath()).thenThrow(e);
            propertyFunctions.getPath().apply(p);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(p.getValue()).thenThrow(e);
            propertyFunctions.getValue().apply(p);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
    }
}
