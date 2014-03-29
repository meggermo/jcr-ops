package nl.meg.jcr.function.internal;

import nl.meg.jcr.exception.RuntimeRepositoryException;
import nl.meg.jcr.function.NodeFunctions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.util.Collections;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeFunctionsImplTest {

    private final NodeFunctions nodeFunctions = new NodeFunctionsImpl();

    @Mock
    private Node node;

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

    @Test
    public void testGetName() throws RepositoryException {
        when(node.getName()).thenReturn("name");
        assertThat(nodeFunctions.getName().apply(node), is("name"));
    }

    @Test
    public void testGePath() throws RepositoryException {
        when(node.getPath()).thenReturn("path");
        assertThat(nodeFunctions.getPath().apply(node), is("path"));
    }

    @Test
    public void testGetIdentifier() throws RepositoryException {
        when(node.getIdentifier()).thenReturn("id");
        assertThat(nodeFunctions.getIdentifier().apply(node), is("id"));
    }

    @Test
    public void testGetPrimaryNodeType() throws RepositoryException {
        when(node.getPrimaryNodeType()).thenReturn(nodeType);
        assertThat(nodeFunctions.getPrimaryNodeType().apply(node), is(nodeType));
    }

    @Test
    public void testGetMixinNodeTypes() throws RepositoryException {
        final NodeType[] nodeTypes = {nodeType};
        when(node.getMixinNodeTypes()).thenReturn(nodeTypes);
        final Iterator<NodeType> result = nodeFunctions.getMixinNodeTypes().apply(node);
        assertThat(result.next(), is(nodeType));
    }

    @Test
    public void testGetIndex() throws RepositoryException {
        when(node.getIndex()).thenReturn(1);
        assertThat(nodeFunctions.getIndex().apply(node), is(1));
    }

    @Test
    public void testGetParent() throws RepositoryException {
        when(node.getParent()).thenReturn(node);
        assertThat(nodeFunctions.getParent().apply(node).get(), is(node));
    }

    @Test
    public void testGetParentOfRoot() throws RepositoryException {
        when(node.getParent()).thenThrow(ItemNotFoundException.class);
        assertThat(nodeFunctions.getParent().apply(node).isPresent(), is(false));
    }

    @Test
    public void testGetNodes() throws RepositoryException {
        when(node.getNodes()).thenReturn(nodeIterator);
        final Iterator<Node> iterator = nodeIterator;
        assertThat(nodeFunctions.getNodes().apply(node), is(iterator));
    }

    @Test
    public void testGetProperties_Empty() throws RepositoryException {
        assertThat(nodeFunctions.getProperties().apply(node), is(Collections.<Property>emptyIterator()));
    }

    @Test
    public void testGetProperties() throws RepositoryException {
        when(node.hasProperties()).thenReturn(true);
        when(node.getProperties()).thenReturn(propertyIterator);
        when(propertyIterator.hasNext()).thenReturn(true, false);
        when(propertyIterator.next()).thenReturn(property);
        assertThat(nodeFunctions.getProperties().apply(node).next(), is(property));
    }

    @Test
    public void testThrowing() throws RepositoryException {
        final Throwable t = e;
        try {
            when(node.getName()).thenThrow(e);
            nodeFunctions.getName().apply(node);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getPath()).thenThrow(e);
            nodeFunctions.getPath().apply(node);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getIdentifier()).thenThrow(e);
            nodeFunctions.getIdentifier().apply(node);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getIndex()).thenThrow(e);
            nodeFunctions.getIndex().apply(node);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getNodes()).thenThrow(e);
            nodeFunctions.getNodes().apply(node);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getParent()).thenThrow(e);
            nodeFunctions.getParent().apply(node);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getPrimaryNodeType()).thenThrow(e);
            nodeFunctions.getPrimaryNodeType().apply(node);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.getMixinNodeTypes()).thenThrow(e);
            nodeFunctions.getMixinNodeTypes().apply(node);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(node.hasProperties()).thenReturn(true);
            when(node.getProperties()).thenThrow(e);
            nodeFunctions.getProperties().apply(node);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
    }

}
