package nl.meg.jcr.traversal.internal;

import com.google.common.collect.Iterators;
import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.HippoNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.RepositoryException;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DescendantTraverserImplTest {

    @Mock
    private HippoNode n0, n1, n2, n3, n4, n5;

    @Mock
    private RepositoryException e;

    private TreeTraverser<HippoNode> traverser;

    @Before
    public void setUp() throws RepositoryException {

        this.traverser = new DescendantTraverserImpl();

        when(n0.getNodes()).thenReturn(Arrays.asList(n1, n2, n3).iterator());
        when(n1.getNodes()).thenReturn(Iterators.<HippoNode>emptyIterator());
        when(n2.getNodes()).thenReturn(Arrays.asList(n4, n5).iterator());
        when(n3.getNodes()).thenReturn(Iterators.<HippoNode>emptyIterator());
        when(n4.getNodes()).thenReturn(Iterators.<HippoNode>emptyIterator());
        when(n5.getNodes()).thenReturn(Iterators.<HippoNode>emptyIterator());
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
