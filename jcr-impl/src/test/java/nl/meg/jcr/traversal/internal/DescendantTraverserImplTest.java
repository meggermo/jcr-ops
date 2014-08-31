package nl.meg.jcr.traversal.internal;

import com.google.common.collect.TreeTraverser;
import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.RepositoryException;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class DescendantTraverserImplTest extends AbstractMockitoTest {

    @Mock
    private HippoNode n0, n1, n2, n3, n4, n5;

    @Mock
    private RepositoryException e;

    private TreeTraverser<HippoNode> traverser;

    @Before
    public void setUp() throws RepositoryException {

        this.traverser = new DescendantTraverserImpl();

        when(n0.getNodes()).thenReturn(Stream.of(n1, n2, n3));
        when(n1.getNodes()).thenReturn(Stream.<HippoNode>empty());
        when(n2.getNodes()).thenReturn(Stream.of(n4, n5));
        when(n3.getNodes()).thenReturn(Stream.<HippoNode>empty());
        when(n4.getNodes()).thenReturn(Stream.<HippoNode>empty());
        when(n5.getNodes()).thenReturn(Stream.<HippoNode>empty());
    }

    @Test
    public void testGetDescendantsPreOrder() throws RepositoryException {
        final List<HippoNode> preOrderResult = traverser.preOrderTraversal(n0).toList();
        assertThat(preOrderResult, is(asList(n0, n1, n2, n4, n5, n3)));
    }

    @Test
    public void testGetDescendantsPostOrder() throws RepositoryException {
        final List<HippoNode> postOrderResult = traverser.postOrderTraversal(n0).toList();
        assertThat(postOrderResult, is(asList(n1, n4, n5, n2, n3, n0)));
    }

    @Test
    public void testGetDescendantsBreadthFirst() throws RepositoryException {
        final List<HippoNode> postOrderResult = traverser.breadthFirstTraversal(n0).toList();
        assertThat(postOrderResult, is(asList(n0, n1, n2, n3, n4, n5)));
    }

}
