package nl.meg.jcr.function.internal;

import nl.meg.jcr.exception.RuntimeRepositoryException;
import nl.meg.jcr.function.ValueFunctions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValueFunctionsImplTest {

    private final ValueFunctions valueFunctions = new ValueFunctionsImpl();

    @Mock
    private Value value;

    @Mock
    private RepositoryException e;

    @Before
    public void setUp() {

    }

    @Test
    public void testGetString() throws RepositoryException {
        when(value.getString()).thenReturn("s");
        assertThat(valueFunctions.getString().apply(value), is("s"));
    }

    @Test
    public void testGetBoolean() throws RepositoryException {
        when(value.getBoolean()).thenReturn(true);
        assertThat(valueFunctions.getBoolean().apply(value), is(true));
    }

    @Test
    public void testGetLong() throws RepositoryException {
        when(value.getLong()).thenReturn(1L);
        assertThat(valueFunctions.getLong().apply(value), is(1L));
    }

    @Test
    public void testGetDate() throws RepositoryException {
        final Calendar calendar = Calendar.getInstance();
        when(value.getDate()).thenReturn(calendar);
        assertThat(valueFunctions.getDate().apply(value), is(calendar));
    }

    @Test
    public void testExceptionTranslation() throws RepositoryException {
        final Throwable t = e;
        try {
            when(value.getString()).thenThrow(e);
            valueFunctions.getString().apply(value);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(value.getLong()).thenThrow(e);
            valueFunctions.getLong().apply(value);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(value.getBoolean()).thenThrow(e);
            valueFunctions.getBoolean().apply(value);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(value.getDate()).thenThrow(e);
            valueFunctions.getDate().apply(value);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
    }
}
