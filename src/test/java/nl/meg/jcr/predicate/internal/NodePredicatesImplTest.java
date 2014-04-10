package nl.meg.jcr.predicate.internal;

import nl.meg.jcr.INode;
import nl.meg.jcr.predicate.NodePredicates;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;
import java.util.Iterator;
import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodePredicatesImplTest {

    private NodePredicates nP;

    @Mock
    private INode n1, n2;

    @Mock
    private Iterator<Property> p1I;

    @Mock
    private Property p1P;

    @Mock
    private Predicate<Property> pP;

    @Mock
    private Predicate<NodeType> ntP;

    @Mock
    private NodeType nodeType;

    @Mock
    private RepositoryException e;

    @Before
    public void setUp() {
        nP = new NodePredicatesImpl();
    }

    @Test
    public void testIsSame() throws RepositoryException {
        when(n1.isSame(n2)).thenReturn(false);
        when(n2.isSame(n1)).thenReturn(true);
        assertThat(nP.isSame(n1).test(n2), is(true));
        assertThat(nP.isSame(n2).test(n1), is(false));
    }

    @Test
    public void testIsNodeType() throws RepositoryException {
        when(n1.isNodeType("X")).thenReturn(true);
        when(n1.isNodeType("Y")).thenReturn(false);
        assertThat(nP.isNodeType("X").test(n1), is(true));
        assertThat(nP.isNodeType("Y").test(n1), is(false));
    }

    @Test
    public void testIdentifierIn() {
        when(n1.getIdentifier()).thenReturn("A");
        assertThat(nP.identifierIn("A", "B").test(n1), is(true));
        assertThat(nP.identifierIn("X", "Y").test(n1), is(false));
    }

    @Test
    public void testNameIn() {
        when(n1.getName()).thenReturn("B");
        assertThat(nP.nameIn("A", "B").test(n1), is(true));
        assertThat(nP.nameIn("X", "Y").test(n1), is(false));
    }

    @Test
    public void testWithProperty() throws RepositoryException {
        when(n1.getProperties()).thenReturn(p1I);
        when(p1I.hasNext()).thenReturn(true, false);
        when(p1I.next()).thenReturn(p1P);
        when(pP.test(p1P)).thenReturn(true);
        assertThat(nP.withProperty(pP).test(n1), is(true));
    }

    @Test
    public void testWithNodeType() {
        when(n1.getPrimaryNodeType()).thenReturn(nodeType);
        when(n1.getMixinNodeTypes()).thenReturn(new NodeType[]{});
        when(ntP.test(nodeType)).thenReturn(true, false);
        assertThat(nP.withNodeType(ntP).test(n1), is(true));
        assertThat(nP.withNodeType(ntP).test(n1), is(false));
    }

}
