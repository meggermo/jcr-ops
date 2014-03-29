package nl.meg.jcr.predicate.internal;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import nl.meg.jcr.function.NodeFunctions;
import nl.meg.jcr.predicate.NodePredicates;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodePredicatesImplTest {

    private NodePredicates nP;

    @Mock
    private NodeFunctions nodeFunctions;

    @Mock
    private Node n1, n2;

    @Mock
    private Function<Node, Iterator<Property>> pF;
    @Mock
    private Function<Node, String> idF, nameF;
    @Mock
    private Iterator<Property> p1I;
    @Mock
    private Property p1P;

    @Mock
    private Predicate<Property> pP;

    @Mock
    private Predicate<NodeType> ntP;

    @Mock
    private Function<Node, Iterator<NodeType>> mntF;

    @Mock
    private Function<Node, NodeType> pntF;

    @Mock
    private NodeType nodeType;

    @Mock
    private RepositoryException e;

    @Before
    public void setUp() {
        nP = new NodePredicatesImpl(nodeFunctions);
        when(nodeFunctions.getIdentifier()).thenReturn(idF);
        when(nodeFunctions.getName()).thenReturn(nameF);
    }

    @Test
    public void testIsSame() throws RepositoryException {
        when(n1.isSame(n2)).thenReturn(true);
        when(n2.isSame(n1)).thenReturn(false);
        assertThat(nP.isSame(n1).apply(n2), is(true));
        assertThat(nP.isSame(n2).apply(n1), is(false));
    }

    @Test
    public void testIsNodeType() throws RepositoryException {
        when(n1.isNodeType("X")).thenReturn(true);
        when(n1.isNodeType("Y")).thenReturn(false);
        assertThat(nP.isNodeType("X").apply(n1), is(true));
        assertThat(nP.isNodeType("Y").apply(n1), is(false));
    }

    @Test
    public void testIdentifierIn() {
        when(idF.apply(n1)).thenReturn("A");
        assertThat(nP.identifierIn("A", "B").apply(n1), is(true));
        assertThat(nP.identifierIn("X", "Y").apply(n1), is(false));
    }

    @Test
    public void testNameIn() {
        when(nameF.apply(n1)).thenReturn("B");
        assertThat(nP.nameIn("A", "B").apply(n1), is(true));
        assertThat(nP.nameIn("X", "Y").apply(n1), is(false));
    }

    @Test
    public void testWithProperty() throws RepositoryException {
        when(nodeFunctions.getProperties()).thenReturn(pF);
        when(pF.apply(n1)).thenReturn(p1I);
        when(p1I.hasNext()).thenReturn(true, false);
        when(p1I.next()).thenReturn(p1P);
        when(pP.apply(p1P)).thenReturn(true);
        assertThat(nP.withProperty(pP).apply(n1), is(true));
    }

    @Test
    public void testWithNodeType() {
        when(nodeFunctions.getMixinNodeTypes()).thenReturn(mntF);
        when(mntF.apply(n1)).thenReturn(Iterators.<NodeType>emptyIterator());
        when(nodeFunctions.getPrimaryNodeType()).thenReturn(pntF);
        when(pntF.apply(n1)).thenReturn(nodeType);
        when(ntP.apply(nodeType)).thenReturn(true, false);
        assertThat(nP.withNodeType(ntP).apply(n1), is(true));
        assertThat(nP.withNodeType(ntP).apply(n1), is(false));
    }

    @Test
    public void testThrowing() throws RepositoryException {
        final Throwable t = e;
        try {
            when(n1.isSame(n2)).thenThrow(e);
            nP.isSame(n1).apply(n2);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(n1.isNodeType("X")).thenThrow(e);
            nP.isNodeType("X").apply(n1);
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
    }

}
