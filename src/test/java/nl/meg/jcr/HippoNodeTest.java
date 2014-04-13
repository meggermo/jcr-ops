package nl.meg.jcr;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class HippoNodeTest extends AbstractMockitoTest {

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
    private RepositoryException e;

    @Mock
    private Session session;

    @Before
    public void setUp() {
        this.hippoNode = new HippoNode() {
            @Override
            public HippoNode apply(Node node) {
                return hippoNode;
            }
            @Override
            public Node get() {
                return node;
            }
        };
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
        when(node.hasNode("X")).thenReturn(false);
        assertThat(hippoNode.getNode("X").isPresent(), is(false));
    }

    @Test
    public void testGetNodes() throws RepositoryException {
        when(node.hasNodes()).thenReturn(true);
        when(node.getNodes()).thenReturn(getNodeIterator(node));
        assertThat(hippoNode.getNodes().get(0).get(), is(node));
    }

    @Test
    public void testGetNodes_Empty() throws RepositoryException {
        when(node.hasNodes()).thenReturn(false);
        assertThat(hippoNode.getNodes().isEmpty(), is(true));
    }

    @Test
    public void testGetExistingProperty() throws RepositoryException {
        when(node.hasProperty("X")).thenReturn(true);
        when(node.getProperty("X")).thenReturn(property);
        assertThat(hippoNode.getProperty("X").get(), is(property));
    }

    @Test
    public void testGetAbsentProperty() throws RepositoryException {
        when(node.hasProperty("X")).thenReturn(false);
        assertThat(hippoNode.getProperty("X").isPresent(), is(false));
    }

    @Test
    public void testGetProperties_Empty() throws RepositoryException {
        when(node.hasProperties()).thenReturn(false);
        assertThat(hippoNode.getProperties().isEmpty(), is(true));
    }

    @Test
    public void testGetProperties() throws RepositoryException {
        when(node.hasProperties()).thenReturn(true);
        when(node.getProperties()).thenReturn(getPropertyIterator(property));
        assertThat(hippoNode.getProperties().get(0), is(property));
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
            hippoNode.getNodes();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.hasNode("X")).thenThrow(e);
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


    private void shouldHaveThrown() {
        shouldHaveThrown(RuntimeRepositoryException.class);
    }

    private NodeIterator getNodeIterator(final Node... nodes) {

        return new NodeIterator() {
            final AtomicInteger i = new AtomicInteger();
            final List<Node> nodeList = Arrays.asList(nodes);
            @Override
            public Node nextNode() {
                return nodeList.get(i.getAndIncrement());
            }

            @Override
            public void skip(long skipNum) {
                i.addAndGet((int)skipNum);
            }

            @Override
            public long getSize() {
                return nodeList.size();
            }

            @Override
            public long getPosition() {
                return i.get();
            }

            @Override
            public boolean hasNext() {
                return i.get() < getSize();
            }

            @Override
            public Object next() {
                return nextNode();
            }
        };
    }

    private PropertyIterator getPropertyIterator(Property... properties) {
        return new PropertyIterator() {
            private final AtomicInteger i = new AtomicInteger();
            private final List<Property> propertyList = Arrays.asList(properties);
            @Override
            public Property nextProperty() {
                return propertyList.get(i.getAndIncrement());
            }

            @Override
            public void skip(long skipNum) {
                i.addAndGet((int)skipNum);
            }

            @Override
            public long getSize() {
                return propertyList.size();
            }

            @Override
            public long getPosition() {
                return i.get();
            }

            @Override
            public boolean hasNext() {
                return i.get() < propertyList.size();
            }

            @Override
            public Object next() {
                return nextProperty();
            }
        };
    }

}
