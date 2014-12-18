package nl.meg.jcr.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.RuntimeRepositoryException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.util.Calendar;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class HippoNodeImplTest extends AbstractMockitoTest {

    private HippoNode hippoNode;

    @Mock
    private Node node, root;

    @Mock
    private HippoNode parent;

    @Mock
    private NodeType nodeType;

    @Mock
    private Property property;

    @Mock
    private Value value;

    @Mock
    private RepositoryException e;

    @Mock
    private Session session;


    @Before
    public void setUp() {
        this.hippoNode = new HippoNodeImpl(node);
    }

    @Test
    public void testGet() {
        assertThat(hippoNode.get(), is(node));
    }

    @Test
    public void testGetParent() throws RepositoryException {
        when(node.getParent()).thenReturn(node);
        assertThat(hippoNode.getParent().get().get(), is(node));
    }

    @Test
    public void testGetParent_IsAbsent() throws RepositoryException {
        when(node.getParent()).thenThrow(ItemNotFoundException.class);
        assertThat(hippoNode.getParent().isPresent(), is(false));
    }

    @Test
    public void testGetPrimaryNodeType() throws RepositoryException {
        when(node.getPrimaryNodeType()).thenReturn(nodeType);
        assertThat(hippoNode.getPrimaryNodeType(), is(nodeType));
    }

    @Test
    public void testGetMixinNodeTypes() throws RepositoryException {
        final NodeType[] nodeTypes = {nodeType};
        when(node.getMixinNodeTypes()).thenReturn(nodeTypes);
        final NodeType[] result = hippoNode.getMixinNodeTypes();
        assertThat(result[0], is(nodeType));
    }

    @Test
    public void testGetIdentifier() throws RepositoryException {
        when(node.getIdentifier()).thenReturn("X");
        assertThat(hippoNode.getIdentifier(), is("X"));
    }

    @Test
    public void testGetIndex() throws RepositoryException {
        when(node.getIndex()).thenReturn(1);
        assertThat(hippoNode.getIndex(), is(1));
    }

    @Test
    public void testGetNode() throws RepositoryException {
        when(node.hasNode("X")).thenReturn(true);
        when(node.getNode("X")).thenReturn(node);
        assertThat(hippoNode.getNode("X").get().get(), is(node));
    }

    @Test
    public void testGetNode_Absent() throws RepositoryException {
        when(node.getNode("X")).thenThrow(new ItemNotFoundException());
        assertThat(hippoNode.getNode("X").isPresent(), is(false));
    }

    @Test
    public void testGetNodes() throws RepositoryException {
        when(node.hasNodes()).thenReturn(true);
        when(node.getNodes()).thenReturn(getNodeIterator(node));
        assertThat(hippoNode.getNodes().collect(toList()).get(0).get(), is(node));
    }

    @Test
    public void testGetNodes_Empty() throws RepositoryException {
        when(node.hasNodes()).thenReturn(false);
        assertThat(hippoNode.getNodes().collect(toList()).isEmpty(), is(true));
    }

    @Test
    public void testGetExistingProperty() throws RepositoryException {
        when(node.hasProperty("X")).thenReturn(true);
        when(node.getProperty("X")).thenReturn(property);
        final HippoProperty p = hippoNode.getProperty("X").get();
        assertThat(p.get(), is(property));
    }

    @Test
    public void testGetAbsentProperty() throws RepositoryException {
        when(node.getProperty("X")).thenThrow(new PathNotFoundException());
        assertThat(hippoNode.getProperty("X").isPresent(), is(false));
    }

    @Test
    public void testGetProperties_Empty() throws RepositoryException {
        when(node.hasProperties()).thenReturn(false);
        assertThat(hippoNode.getProperties().collect(toList()).isEmpty(), is(true));
    }

    @Test
    public void testGetProperties() throws RepositoryException {
        when(node.hasProperties()).thenReturn(true);
        when(node.getProperties()).thenReturn(getPropertyIterator(property));
        final HippoProperty p = hippoNode.getProperties().collect(toList()).get(0);
        assertThat(p.get(), is(property));
    }

    @Test
    public void testIsRoot() throws RepositoryException {
        when(node.getSession()).thenReturn(session);
        when(session.getRootNode()).thenReturn(node);
        when(node.isSame(node)).thenReturn(true);
        assertThat(hippoNode.isRoot(), is(true));
    }

    @Test(expected = RuntimeRepositoryException.class)
    public void testIsRootThrows() throws RepositoryException {
        when(node.getSession()).thenThrow(RepositoryException.class);
        hippoNode.isRoot();
        shouldHaveThrown();
    }

    @Test
    public void testIsNodeType() throws RepositoryException {
        when(node.isNodeType("X")).thenReturn(true);
        assertThat(hippoNode.isNodeType("X"), is(true));
        assertThat(hippoNode.isNodeType("T"), is(false));
    }

    @Test
    public void testExceptionTranslation() throws RepositoryException {
        final Throwable t = e;
        try {
            when(node.getIdentifier()).thenThrow(e);
            hippoNode.getIdentifier();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getIndex()).thenThrow(e);
            hippoNode.getIndex();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.hasNodes()).thenThrow(e);
            hippoNode.getNodes().collect(toList());
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getNode("X")).thenThrow(e);
            hippoNode.getNode("X");
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getPrimaryNodeType()).thenThrow(e);
            hippoNode.getPrimaryNodeType();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getMixinNodeTypes()).thenThrow(e);
            hippoNode.getMixinNodeTypes();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.hasProperties()).thenReturn(true);
            when(node.getProperties()).thenThrow(e);
            hippoNode.getProperties();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.hasProperty("X")).thenReturn(true);
            when(node.getProperty("X")).thenThrow(e);
            hippoNode.getProperty("X");
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.isNodeType("X")).thenThrow(e);
            hippoNode.isNodeType("X");
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
    }

    @Test
    public void testGetString() throws RepositoryException {
        when(node.getProperty(anyString())).thenReturn(property);
        when(property.getValue()).thenReturn(value);
        when(value.getString()).thenReturn(null, "string");
        assertThat(hippoNode.getString("empty").isPresent(), is(false));
        assertThat(hippoNode.getString("present").get(), is("string"));
    }

    @Test
    public void testGetBoolean() throws RepositoryException {
        when(node.getProperty(anyString())).thenReturn(property);
        when(property.getValue()).thenReturn(value);
        when(value.getBoolean()).thenReturn(false, true);
        assertThat(hippoNode.getBoolean("empty"), is(false));
        assertThat(hippoNode.getBoolean("empty"), is(true));
    }

    @Test
    public void testGetDate() throws RepositoryException {
        when(node.getProperty(anyString())).thenReturn(property);
        when(property.getValue()).thenReturn(value);
        final Calendar someDate = Calendar.getInstance();
        when(value.getDate()).thenReturn(null, someDate);
        assertThat(hippoNode.getDate("empty").isPresent(), is(false));
        assertThat(hippoNode.getDate("present").get(), is(someDate));
    }
}
