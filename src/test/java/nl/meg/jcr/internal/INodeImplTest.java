package nl.meg.jcr.internal;

import com.google.common.base.Optional;
import nl.meg.jcr.INode;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class INodeImplTest {

    private INode iNode;

    @Mock
    private Node node, root;

    @Mock
    private Optional<INode> parent;

    @Mock
    private NodeType nodeType;

    @Mock
    private NodeIterator nodeIterator;

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
        this.iNode = new INodeImpl(node);
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
    public void testGetNodes() throws RepositoryException {
        when(node.hasNodes()).thenReturn(true);
        when(node.getNodes()).thenReturn(nodeIterator);
        when(nodeIterator.hasNext()).thenReturn(true,false);
        when(nodeIterator.next()).thenReturn(node);
        assertThat(iNode.getNodes().next().get(), is(node));
    }

    @Test
    public void testHasNodes() throws RepositoryException {
        assertThat(iNode.hasNodes(), is(false));
    }

    @Test
    public void testHasNode() {
        assertThat(iNode.hasNode("X"), is(false));
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
        assertThat(iNode.getProperty("X"), is(property));
    }

    @Test
    public void testGetAbsentProperty() throws RepositoryException {
        when(node.hasProperty("X")).thenReturn(false);
        assertThat(iNode.getProperty("X"), is(nullValue()));
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

    @Test
    public void testIsSame() throws RepositoryException {
        when(node.isSame(node)).thenReturn(true);
        assertThat(iNode.isSame(iNode), is(true));
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
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getName()).thenThrow(e);
            iNode.getName();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getPath()).thenThrow(e);
            iNode.getPath();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getIdentifier()).thenThrow(e);
            iNode.getIdentifier();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getIndex()).thenThrow(e);
            iNode.getIndex();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getNodes()).thenThrow(e);
            iNode.getNodes();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getParent()).thenThrow(e);
            iNode.getParent();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getPrimaryNodeType()).thenThrow(e);
            iNode.getPrimaryNodeType();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getMixinNodeTypes()).thenThrow(e);
            iNode.getMixinNodeTypes();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.hasProperties()).thenReturn(true);
            when(node.getProperties()).thenThrow(e);
            iNode.getProperties();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.hasProperty("X")).thenReturn(true);
            when(node.getProperty("X")).thenThrow(e);
            iNode.getProperty("X");
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
    }

}
