package nl.meg.jcr.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoValue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Binary;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.math.BigDecimal;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class HippoValueImplTest extends AbstractMockitoTest {

    private HippoValue hippoValue;

    @Mock
    private Value value;

    @Mock
    private Binary binary;

    @Before
    public void setUp() {
        this.hippoValue = new HippoValueImpl(value);
    }

    @Test
    public void testGetBinary() throws RepositoryException {
        when(value.getBinary()).thenReturn(binary);
        assertThat(hippoValue.getBinary(), is(binary));
    }

    @Test
    public void testGetBoolean() throws RepositoryException {
        when(value.getBoolean()).thenReturn(true);
        assertThat(hippoValue.getBoolean(), is(true));
    }

    @Test
    public void testGetDate() throws RepositoryException {
        final Calendar date = Calendar.getInstance();
        when(value.getDate()).thenReturn(date);
        assertThat(hippoValue.getDate(),is(date));
    }

    @Test
    public void testGetDecimal() throws RepositoryException {
        final BigDecimal decimal = BigDecimal.ONE;
        when(value.getDecimal()).thenReturn(decimal);
        assertThat(hippoValue.getDecimal(), is(decimal));
    }

    @Test
    public void testGetDouble() throws RepositoryException {
        when(value.getDouble()).thenReturn(1.0D);
        assertThat(hippoValue.getDouble(), is(1.0D));
    }

    @Test
    public void testGetLong() throws RepositoryException {
        when(value.getLong()).thenReturn(1L);
        assertThat(hippoValue.getLong(), is(1L));
    }

    @Test
    public void testGetString() throws RepositoryException {
        when(value.getString()).thenReturn("X");
        assertThat(hippoValue.getString(), is("X"));
    }

    @Test
    public void testGet() {
        assertThat(hippoValue.get(), is(value));
    }
}
