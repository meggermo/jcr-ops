package nl.meg.jcr.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoNode;
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
    private Session session;

    @Before
    public void setUp() {
        this.hippoNode = new HippoNodeImpl(node);
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

    @Test
    public void testIsNodeType() throws RepositoryException {
        when(node.isNodeType("X")).thenReturn(true);
        assertThat(hippoNode.isNodeType("X"), is(true));
        assertThat(hippoNode.isNodeType("T"), is(false));
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
                i.addAndGet((int) skipNum);
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
                i.addAndGet((int) skipNum);
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
