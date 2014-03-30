package nl.meg.jcr.traversal.internal;

import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DescendantTraverserImplTest {

    @Mock
    private Node n0, n1, n2, n3, n4, n5;

    @Mock
    private NodeIterator i0, i2;

    @Mock
    private RepositoryException e;

    private TreeTraverser<Node> traverser;

    @Before
    public void setUp() throws RepositoryException {

        this.traverser = new DescendantTraverserImpl();

        when(n0.hasNodes()).thenReturn(true);
        when(n0.getNodes()).thenReturn(i0);
        when(i0.hasNext()).thenReturn(true, true, true, false);
        when(i0.next()).thenReturn(n1, n2, n3);
        when(n2.hasNodes()).thenReturn(true);
        when(n2.getNodes()).thenReturn(i2);
        when(i2.hasNext()).thenReturn(true, true, false);
        when(i2.next()).thenReturn(n4, n5);
    }

    @Test
    public void testGetDescendantsPreOrder() throws RepositoryException {
        final List<Node> preOrderResult = traverser.preOrderTraversal(n0).toList();
        assertThat(preOrderResult, is(asList(n0, n1, n2, n4, n5, n3)));
    }

    @Test
    public void testGetDescendantsPostOrder() throws RepositoryException {
        final List<Node> postOrderResult = traverser.postOrderTraversal(n0).toList();
        assertThat(postOrderResult, is(asList(n1, n4, n5, n2, n3, n0)));
    }

    @Test
    public void testGetDescendantsBreadthFirst() throws RepositoryException {
        final List<Node> postOrderResult = traverser.breadthFirstTraversal(n0).toList();
        assertThat(postOrderResult, is(asList(n0, n1, n2, n3, n4, n5)));
    }

    @Test
    public void testExceptionTranslation() throws RepositoryException {
        final Throwable t = e;
        when(n0.getNodes()).thenThrow(e);
        try {
            traverser.breadthFirstTraversal(n0).toList();
        } catch (RuntimeRepositoryException rre) {
            assertThat(rre.getCause(), is(t));
        }
    }

}
