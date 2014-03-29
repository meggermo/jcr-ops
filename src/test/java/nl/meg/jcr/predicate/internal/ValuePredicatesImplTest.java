package nl.meg.jcr.predicate.internal;

import com.google.common.base.Function;
import nl.meg.jcr.function.ValueFunctions;
import nl.meg.jcr.predicate.ValuePredicates;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Value;

import java.lang.Boolean;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValuePredicatesImplTest {

    private ValuePredicates valuePredicates;

    @Mock
    private ValueFunctions valueFunctions;

    @Mock
    private Value value;

    @Mock
    private Function<Value, Boolean> boolF;

    @Mock
    private Function<Value, String> stringF;

    @Mock
    private Function<Value, Long> longF;

    @Mock
    private Function<Value, Calendar> dateF;

    @Before
    public void setUp() {
        this.valuePredicates = new ValuePredicatesImpl(valueFunctions);
    }

    @Test
    public void testEqualToBoolean() {
        when(valueFunctions.getBoolean()).thenReturn(boolF);
        when(boolF.apply(value)).thenReturn(true,false);
        assertThat(valuePredicates.equalTo(true).apply(value), is(true));
        assertThat(valuePredicates.equalTo(true).apply(value), is(false));
    }

    @Test
    public void testEqualToString() {
        when(valueFunctions.getString()).thenReturn(stringF);
        when(stringF.apply(value)).thenReturn("X","Y");
        assertThat(valuePredicates.equalTo("X").apply(value), is(true));
        assertThat(valuePredicates.equalTo("Y").apply(value), is(true));
    }

    @Test
    public void testEqualToLong() {
        when(valueFunctions.getLong()).thenReturn(longF);
        when(longF.apply(value)).thenReturn(1L);
        assertThat(valuePredicates.equalTo(1L).apply(value), is(true));
        assertThat(valuePredicates.equalTo(2L).apply(value), is(false));
    }

    @Test
    public void testEqualTo3() {
        when(valueFunctions.getDate()).thenReturn(dateF);
        final GregorianCalendar calendar = new GregorianCalendar();
        when(dateF.apply(value)).thenReturn(calendar);
        assertThat(valuePredicates.equalTo(calendar).apply(value), is(true));
    }
}
