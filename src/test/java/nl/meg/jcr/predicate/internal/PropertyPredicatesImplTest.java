package nl.meg.jcr.predicate.internal;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import nl.meg.jcr.function.PropertyFunctions;
import nl.meg.jcr.predicate.PropertyPredicates;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Property;
import javax.jcr.Value;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PropertyPredicatesImplTest {

    private PropertyPredicates propertyPredicates;

    @Mock
    private PropertyFunctions pF;

    @Mock
    private Property p;

    @Mock
    private Function<Property, String> nameF;

    @Mock
    private Predicate<Value> vP;

    @Mock
    private Function<Property, Value> vF;

    @Mock
    private Value v;

    @Before
    public void setUp() {
        propertyPredicates = new PropertyPredicatesImpl(pF);
    }

    @Test
    public void testNameIn() {
        when(pF.getName()).thenReturn(nameF);
        when(nameF.apply(p)).thenReturn("X");
        assertThat(propertyPredicates.nameIn("X", "Y").apply(p), is(true));
        assertThat(propertyPredicates.nameIn("A", "B").apply(p), is(false));
    }

    @Test
    public void testPathIn() {
        when(pF.getPath()).thenReturn(nameF);
        when(nameF.apply(p)).thenReturn("X");
        assertThat(propertyPredicates.pathIn("X", "Y").apply(p), is(true));
        assertThat(propertyPredicates.pathIn("A", "B").apply(p), is(false));
    }

    @Test
    public void testWithValuePredicate() {
        when(pF.getValue()).thenReturn(vF);
        when(vF.apply(p)).thenReturn(v);
        when(vP.apply(v)).thenReturn(true, false);
        assertThat(propertyPredicates.with(vP).apply(p), is(true));
        assertThat(propertyPredicates.with(vP).apply(p), is(false));
    }

    @Test
    public void testWithNamedValue() {
        when(pF.getName()).thenReturn(nameF);
        when(nameF.apply(p)).thenReturn("X");
        when(pF.getValue()).thenReturn(vF);
        when(vF.apply(p)).thenReturn(v);
        when(vP.apply(v)).thenReturn(true, false);
        assertThat(propertyPredicates.with(vP).apply(p), is(true));
        assertThat(propertyPredicates.with("X", vP).apply(p), is(false));

    }
}
