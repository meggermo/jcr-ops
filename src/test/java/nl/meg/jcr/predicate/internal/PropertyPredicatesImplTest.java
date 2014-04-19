package nl.meg.jcr.predicate.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.predicate.PropertyPredicates;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.Optional;
import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class PropertyPredicatesImplTest extends AbstractMockitoTest {

    private PropertyPredicates propertyPredicates;

    @Mock
    private HippoProperty p;

    @Mock
    private Predicate<Value> vP;

    @Mock
    private Value v;

    @Before
    public void setUp() {
        propertyPredicates = new PropertyPredicatesImpl();
    }

    @Test
    public void testNameIn() throws RepositoryException {
        when(p.getName()).thenReturn("X");
        assertThat(propertyPredicates.nameIn("X", "Y").test(p), is(true));
        assertThat(propertyPredicates.nameIn("A", "B").test(p), is(false));
    }

    @Test
    public void testPathIn() throws RepositoryException {
        when(p.getPath()).thenReturn("X");
        assertThat(propertyPredicates.pathIn("X", "Y").test(p), is(true));
        assertThat(propertyPredicates.pathIn("A", "B").test(p), is(false));
    }

    @Test
    public void testWithValuePredicate() throws RepositoryException {
        when(p.getValue()).thenReturn(Optional.of(v));
        when(vP.test(v)).thenReturn(true, false);
        assertThat(propertyPredicates.with(vP).test(p), is(true));
        assertThat(propertyPredicates.with(vP).test(p), is(false));
    }

    @Test
    public void testWithNamedValue() throws RepositoryException {
        when(p.getValue()).thenReturn(Optional.of(v));
        when(p.getName()).thenReturn("X");
        when(vP.test(v)).thenReturn(true, false);
        assertThat(propertyPredicates.with(vP).test(p), is(true));
        assertThat(propertyPredicates.with("X", vP).test(p), is(false));
    }
}
