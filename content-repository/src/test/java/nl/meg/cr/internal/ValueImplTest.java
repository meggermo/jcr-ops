package nl.meg.cr.internal;

import nl.meg.cr.Value;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.RepositoryException;
import java.math.BigDecimal;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ValueImplTest extends AbstractMockitoTest {

    @Mock
    private javax.jcr.Value v;

    private Value value;

    @Before
    public void setUp() {
        this.value = new ValueImpl(v);
    }

    @Test
    public void testGetBoolean() throws RepositoryException {
        when(v.getBoolean()).thenReturn(true);
        assertThat(value.getBoolean(), is(true));
    }

    @Test
    public void testGetDate() throws RepositoryException {
        Calendar date = Calendar.getInstance();
        when(v.getDate()).thenReturn(date);
        assertThat(value.getDate(), is(date));
    }

    @Test
    public void testGetDecimal() throws RepositoryException {
        when(v.getDecimal()).thenReturn(BigDecimal.ONE);
        assertThat(value.getDecimal(), is(BigDecimal.ONE));
    }

    @Test
    public void testGetDouble() throws RepositoryException {
        when(v.getDouble()).thenReturn(1.0D);
        assertThat(value.getDouble(), is(1.0D));
    }

    @Test
    public void testGetLong() throws RepositoryException {
        when(v.getLong()).thenReturn(Long.MIN_VALUE);
        assertThat(value.getLong(), is(Long.MIN_VALUE));
    }

    @Test
    public void testGetString() throws RepositoryException {
        when(v.getString()).thenReturn("test");
        assertThat(value.getString(), is("test"));
    }

    @Test
    public void testGetEnum() throws RepositoryException {
        when(v.getString()).thenReturn("test");
        assertThat(value.getEnum(E.class), is(E.test));
    }

    @Test
    public void testHashCode() {
        assertThat(value.equals(null), is(false));
        assertThat(value.equals("test"), is(false));
        assertThat(value.equals(value), is(true));
        assertThat(value.hashCode(), is(new ValueImpl(v).hashCode()));
    }

    private enum E {
        test
    }
}
