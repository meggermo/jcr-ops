package nl.meg.jcr.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.MutableHippoNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class MutableHippoNodeImplTest extends AbstractMockitoTest {

    private MutableHippoNode mutableHippoNode;

    @Mock
    private Node node;

    @Mock
    private Session session;

    @Mock
    private ValueFactory factory;

    @Before
    public void setUp() throws RepositoryException {
        this.mutableHippoNode = new MutableHippoNodeImpl(node);
        when(node.getSession()).thenReturn(session);
        when(session.getValueFactory()).thenReturn(factory);
    }

    @Test
    public void testSetPrimaryType() {
        assertThat(mutableHippoNode.setPrimaryType("test").get(), is(node));
    }

    @Test
    public void testAddMixinType() {
        assertThat(mutableHippoNode.addMixinType("test").get(), is(node));
    }

    @Test
    public void testSetBooleanProperty() {
        assertThat(mutableHippoNode.setProperty("test", true).get(), is(node));
        assertThat(mutableHippoNode.setProperty("test", true, false).get(), is(node));
    }

    @Test
    public void testSetStringProperty() {
        assertThat(mutableHippoNode.setProperty("test", "test").get(), is(node));
        assertThat(mutableHippoNode.setProperty("test", "test1", "test2").get(), is(node));
    }

    @Test
    public void testSetCalendarProperty() {
        final Calendar instance = Calendar.getInstance();
        assertThat(mutableHippoNode.setProperty("test", instance).get(), is(node));
        assertThat(mutableHippoNode.setProperty("test", instance, instance).get(), is(node));
    }

    @Test
    public void testSetLongProperty() {
        assertThat(mutableHippoNode.setProperty("test", 1L).get(), is(node));
        assertThat(mutableHippoNode.setProperty("test", 1L, 2L).get(), is(node));
    }

    @Test
    public void testSetProperty4() {
        assertThat(mutableHippoNode.setProperty("test", TEST.A).get(), is(node));
        assertThat(mutableHippoNode.setProperty("test", TEST.A, TEST.A).get(), is(node));
    }

    enum TEST {A};
}
