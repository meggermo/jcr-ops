package nl.meg.jcr.traversal.internal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AncestorTraverserImplTest {

    private final TreeTraverser<Node> traverser = new AncestorTraverserImpl();

    @Mock
    private Node n0, n1, n2, n3, n4;

    @Mock
    private RepositoryException e;

    @Before
    public void setUp() throws RepositoryException {
        when(n0.getParent()).thenReturn(n1);
        when(n1.getName()).thenReturn("n1");
        when(n1.getParent()).thenReturn(n2);
        when(n2.getName()).thenReturn("n2");
        when(n2.getParent()).thenReturn(n3);
        when(n3.getName()).thenReturn("n3");
        when(n3.getParent()).thenReturn(n4);
        when(n4.getName()).thenReturn("n4");
        when(n4.getParent()).thenThrow(ItemNotFoundException.class);
    }

    @Test
    public void testTraversePreOrder() {
        final List<Node> ancestors = traverser.preOrderTraversal(n0).toList();
        assertThat(ancestors, is(asList(n0, n1, n2, n3, n4)));
    }

    @Test
    public void testTraversePostOrder() {
        final List<Node> ancestors = traverser.postOrderTraversal(n0).toList();
        assertThat(ancestors, is(asList(n4, n3, n2, n1, n0)));
    }

    @Test
    public void testTraverseBreadthFirst() {
        final List<Node> ancestors = traverser.breadthFirstTraversal(n0).toList();
        assertThat(ancestors, is(asList(n0, n1, n2, n3, n4)));
    }

    @Test
    public void testExceptionTranslation() throws RepositoryException {
        final Throwable t = e;
        when(n3.getParent()).thenThrow(e);
        try {
            traverser.breadthFirstTraversal(n0).toList();
        } catch (RuntimeRepositoryException rre) {
            assertThat(rre.getCause(), is(t));
        }
    }
}
