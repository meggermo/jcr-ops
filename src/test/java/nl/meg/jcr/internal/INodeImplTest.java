package nl.meg.jcr.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class INodeImplTest {

    private HippoNode iNode;

    @Mock
    private Node node, root;

    @Mock
    private HippoNode parent;

    @Mock
    private NodeType nodeType;

    @Mock
    private PropertyIterator propertyIterator;

    @Mock
    private Property property;

    @Mock
    private RepositoryException e;

    @Mock
    private Session session;

    @Before
    public void setUp() {
        this.iNode = new HippoNodeImpl(node);
    }
    @Test
    public void testGetSession() throws RepositoryException {
        when(node.getSession()).thenReturn(session);
        assertThat(iNode.getSession(), is(session));
    }

    @Test
    public void testGetName() throws RepositoryException {
        when(node.getName()).thenReturn("name");
        assertThat(iNode.getName(), is("name"));
    }

    @Test
    public void testGePath() throws RepositoryException {
        when(node.getPath()).thenReturn("path");
        assertThat(iNode.getPath(), is("path"));
    }

    @Test
    public void testGetIdentifier() throws RepositoryException {
        when(node.getIdentifier()).thenReturn("id");
        assertThat(iNode.getIdentifier(), is("id"));
    }

    @Test
    public void testGetPrimaryNodeType() throws RepositoryException {
        when(node.getPrimaryNodeType()).thenReturn(nodeType);
        assertThat(iNode.getPrimaryNodeType(), is(nodeType));
    }

    @Test
    public void testGetMixinNodeTypes() throws RepositoryException {
        final NodeType[] nodeTypes = {nodeType};
        when(node.getMixinNodeTypes()).thenReturn(nodeTypes);
        final NodeType[] result = iNode.getMixinNodeTypes();
        assertThat(result[0], is(nodeType));
    }

    @Test
    public void testGetIndex() throws RepositoryException {
        when(node.getIndex()).thenReturn(1);
        assertThat(iNode.getIndex(), is(1));
    }

    @Test
    public void testGetParent() throws RepositoryException {
        when(node.getParent()).thenReturn(node);
        assertThat(iNode.getParent().get().get(), is(node));
    }

    @Test
    public void testGetParentOfRoot() throws RepositoryException {
        when(node.getParent()).thenThrow(ItemNotFoundException.class);
        assertThat(iNode.getParent().isPresent(), is(false));
    }

    @Test
    public void testGetNode() throws RepositoryException {
        when(node.hasNode("X")).thenReturn(true);
        when(node.getNode("X")).thenReturn(node);
        assertThat(iNode.getNode("X").get().get(), is(node));
    }

    @Test
    public void testGetNode_Absent() throws RepositoryException {
        when(node.hasNode("X")).thenReturn(false);
        assertThat(iNode.getNode("X").isPresent(), is(false));
    }

    @Test
    public void testGetNodes() throws RepositoryException {
        when(node.hasNodes()).thenReturn(true);
        when(node.getNodes()).thenReturn(getNodeIterator(node));
        assertThat(iNode.getNodes().next().get(), is(node));
    }

    @Test
    public void testGetNodes_Empty() throws RepositoryException {
        when(node.hasNodes()).thenReturn(false);
        assertThat(iNode.getNodes().hasNext(), is(false));
    }

    @Test
    public void testGetExistingProperty() throws RepositoryException {
        when(node.hasProperty("X")).thenReturn(true);
        when(node.getProperty("X")).thenReturn(property);
        assertThat(iNode.getProperty("X").get(), is(property));
    }

    @Test
    public void testGetAbsentProperty() throws RepositoryException {
        when(node.hasProperty("X")).thenReturn(false);
        assertThat(iNode.getProperty("X").isPresent(), is(false));
    }

    @Test
    public void testGetProperties_Empty() throws RepositoryException {
        when(node.hasProperties()).thenReturn(false);
        assertThat(iNode.getProperties(), is(Collections.<Property>emptyIterator()));
    }

    @Test
    public void testGetProperties() throws RepositoryException {
        when(node.hasProperties()).thenReturn(true);
        when(node.getProperties()).thenReturn(propertyIterator);
        when(propertyIterator.hasNext()).thenReturn(true, false);
        when(propertyIterator.next()).thenReturn(property);
        assertThat(iNode.getProperties().next(), is(property));
    }

    @Test
    public void testIsRoot() throws RepositoryException {
        when(node.getSession()).thenReturn(session);
        when(session.getRootNode()).thenReturn(node);
        when(node.isSame(node)).thenReturn(true);
        assertThat(iNode.isRoot(), is(true));
    }

    @Test(expected = RuntimeRepositoryException.class)
    public void testIsRootThrows() throws RepositoryException {
        when(node.getSession()).thenThrow(RepositoryException.class);
        iNode.isRoot();
        shouldHaveThrown();
    }

    @Test
    public void testIsSame() throws RepositoryException {
        when(node.isSame(node)).thenReturn(true);
        assertThat(iNode.isSame(iNode), is(true));
    }

    @Test(expected = RuntimeRepositoryException.class)
    public void testIsSameThrows() throws RepositoryException {
        when(node.isSame(node)).thenThrow(RepositoryException.class);
        iNode.isSame(iNode);
        shouldHaveThrown();
    }

    @Test
    public void testIsNodeType() throws RepositoryException {
        when(node.isNodeType("X")).thenReturn(true);
        assertThat(iNode.isNodeType("X"), is(true));
        assertThat(iNode.isNodeType("T"), is(false));
    }

    @Test
    public void testExceptionTranslation() throws RepositoryException {
        final Throwable t = e;
        try {
            when(node.getSession()).thenThrow(e);
            iNode.getSession();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getName()).thenThrow(e);
            iNode.getName();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getPath()).thenThrow(e);
            iNode.getPath();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getIdentifier()).thenThrow(e);
            iNode.getIdentifier();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getIndex()).thenThrow(e);
            iNode.getIndex();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.hasNodes()).thenThrow(e);
            iNode.getNodes();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.hasNode("X")).thenThrow(e);
            iNode.getNode("X");
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getParent()).thenThrow(e);
            iNode.getParent();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getPrimaryNodeType()).thenThrow(e);
            iNode.getPrimaryNodeType();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getMixinNodeTypes()).thenThrow(e);
            iNode.getMixinNodeTypes();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.hasProperties()).thenReturn(true);
            when(node.getProperties()).thenThrow(e);
            iNode.getProperties();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.hasProperty("X")).thenReturn(true);
            when(node.getProperty("X")).thenThrow(e);
            iNode.getProperty("X");
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.isNodeType("X")).thenThrow(e);
            iNode.isNodeType("X");
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
    }

    private void shouldHaveThrown() {
        fail("expected exception to be thrown");
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

}
