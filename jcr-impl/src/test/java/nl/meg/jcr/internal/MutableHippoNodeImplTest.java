package nl.meg.jcr.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.MutableHippoNode;
import nl.meg.jcr.RuntimeRepositoryException;
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
    public void testSetBoolean() {
        assertThat(mutableHippoNode.setBoolean("test", true).get(), is(node));
        assertThat(mutableHippoNode.setBoolean("test", true, false).get(), is(node));
    }

    @Test
    public void testSetString() {
        assertThat(mutableHippoNode.setString("test", "test").get(), is(node));
        assertThat(mutableHippoNode.setString("test", "test1", "test2").get(), is(node));
    }

    @Test
    public void testSetCalendar() {
        final Calendar instance = Calendar.getInstance();
        assertThat(mutableHippoNode.setDate("test", instance).get(), is(node));
        assertThat(mutableHippoNode.setDate("test", instance, instance).get(), is(node));
    }

    @Test
    public void testSetLong() {
        assertThat(mutableHippoNode.setLong("test", 1L).get(), is(node));
        assertThat(mutableHippoNode.setLong("test", 1L, 2L).get(), is(node));
    }

    @Test
    public void testSetEnum() {
        assertThat(mutableHippoNode.setEnum("test", TEST.A).get(), is(node));
        assertThat(mutableHippoNode.setEnum("test", TEST.A, TEST.A).get(), is(node));
    }

    @Test
    public void testAddNode() throws RepositoryException {
        when(node.addNode("test")).thenReturn(node);
        assertThat(mutableHippoNode.addNode("test").get(), is(node));
    }

    @Test(expected = RuntimeRepositoryException.class)
    public void testAddNode_throws() throws RepositoryException {
        when(node.addNode("test")).thenThrow(RepositoryException.class);
        assertThat(mutableHippoNode.addNode("test").get(), is(node));
    }

    enum TEST {A}
}
