package nl.meg.jcr.predicate.internal;

import nl.meg.jcr.predicate.ValuePredicates;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.GregorianCalendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValuePredicatesImplTest {

    private ValuePredicates valuePredicates;

    @Mock
    private Value value;

    @Before
    public void setUp() {
        this.valuePredicates = new ValuePredicatesImpl();
    }

    @Test
    public void testEqualToBoolean() throws RepositoryException {
        when(value.getBoolean()).thenReturn(true, false);
        assertThat(valuePredicates.equalTo(true).test(value), is(true));
        assertThat(valuePredicates.equalTo(true).test(value), is(false));
    }

    @Test
    public void testEqualToString() throws RepositoryException {
        when(value.getString()).thenReturn("X", "Y");
        assertThat(valuePredicates.equalTo("X").test(value), is(true));
        assertThat(valuePredicates.equalTo("Y").test(value), is(true));
    }

    @Test
    public void testEqualToLong() throws RepositoryException {
        when(value.getLong()).thenReturn(1L);
        assertThat(valuePredicates.equalTo(1L).test(value), is(true));
        assertThat(valuePredicates.equalTo(2L).test(value), is(false));
    }

    @Test
    public void testEqualTo3() throws RepositoryException {
        final GregorianCalendar calendar = new GregorianCalendar();
        when(value.getDate()).thenReturn(calendar);
        assertThat(valuePredicates.equalTo(calendar).test(value), is(true));
    }
}
