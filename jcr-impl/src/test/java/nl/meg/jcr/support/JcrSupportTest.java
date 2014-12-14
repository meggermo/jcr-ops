package nl.meg.jcr.support;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.HippoValue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Calendar;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class JcrSupportTest extends AbstractMockitoTest {

    @Mock
    private HippoNode node;

    @Mock
    private HippoProperty property;

    @Mock
    private HippoValue value;

    private final Calendar someDate = Calendar.getInstance();

    @Before
    public void setUp() {
        when(node.getProperty("empty")).thenReturn(Optional.<HippoProperty>empty());
        when(node.getProperty("present")).thenReturn(Optional.of(property));
        when(property.getValue()).thenReturn(Optional.of(value));
        when(value.getString()).thenReturn("string");
        when(value.getDate()).thenReturn(someDate);
    }

    @Test
    public void testGetString() {
        assertThat(JcrSupport.getString(node, "empty").isPresent(), is(false));
        assertThat(JcrSupport.getString(node, "present").get(), is("string"));
    }

    @Test
    public void testGetDate() {
        assertThat(JcrSupport.getDate(node, "empty").isPresent(), is(false));
        assertThat(JcrSupport.getDate(node, "present").get(), is(someDate));
    }
}
