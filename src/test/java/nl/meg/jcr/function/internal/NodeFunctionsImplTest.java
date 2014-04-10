package nl.meg.jcr.function.internal;

import nl.meg.jcr.INode;
import nl.meg.jcr.function.NodeFunctions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeFunctionsImplTest {

    private final NodeFunctions nodeFunctions = new NodeFunctionsImpl();

    @Mock
    private INode node;

    @Mock
    private INode parent;

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

    @Test
    public void testGetSession() throws RepositoryException {
        when(node.getSession()).thenReturn(session);
        assertThat(nodeFunctions.getSession().apply(node), is(session));
    }

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
        when(node.getParent()).thenReturn(Optional.of(parent));
        assertThat(nodeFunctions.getParent().apply(node).get(), is(parent));
    }

    @Test
    public void testGetParentOfRoot() throws RepositoryException {
        when(node.getParent()).thenReturn(Optional.<INode>empty());
        assertThat(nodeFunctions.getParent().apply(node).isPresent(), is(false));
    }

    @Test
    public void testGetNodes() throws RepositoryException {
        when(node.getNodes()).thenReturn(nodeIterator);
        final Iterator<INode> iterator = nodeIterator;
        assertThat(nodeFunctions.getNodes().apply(node), is(iterator));
    }

    @Test
    public void testGetExistingProperty() throws RepositoryException {
        when(node.getProperty("X")).thenReturn(Optional.of(property));
        assertThat(nodeFunctions.getProperty("X").apply(node).get(), is(property));
    }

    @Test
    public void testGetAbsentProperty() throws RepositoryException {
        when(node.getProperty("X")).thenReturn(Optional.<Property>empty());
        assertThat(nodeFunctions.getProperty("X").apply(node).isPresent(), is(false));
    }

    @Test
    public void testGetProperties_Empty() throws RepositoryException {
        when(node.getProperties()).thenReturn(Collections.<Property>emptyIterator());
        assertThat(nodeFunctions.getProperties().apply(node), is(Collections.<Property>emptyIterator()));
    }

    @Test
    public void testGetProperties() throws RepositoryException {
        when(node.getProperties()).thenReturn(propertyIterator);
        when(propertyIterator.hasNext()).thenReturn(true, false);
        when(propertyIterator.next()).thenReturn(property);
        assertThat(nodeFunctions.getProperties().apply(node).next(), is(property));
    }

}
