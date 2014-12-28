package nl.meg.jcr.traversal.internal;

import nl.meg.jcr.HippoNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.RepositoryException;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AncestorTraverserImplTest {

    private final TreeTraverser<HippoNode> traverser = new AncestorTraverserImpl();

    @Mock
    private HippoNode n0, n1, n2, n3, n4;

    @Mock
    private RepositoryException e;

    @Before
    public void setUp() throws RepositoryException {

        when(n0.getParent()).thenReturn(Optional.of(n1));

        when(n1.getName()).thenReturn("n1");
        when(n1.getParent()).thenReturn(Optional.of(n2));

        when(n2.getName()).thenReturn("n2");
        when(n2.getParent()).thenReturn(Optional.of(n3));

        when(n3.getName()).thenReturn("n3");
        when(n3.getParent()).thenReturn(Optional.of(n4));

        when(n4.getName()).thenReturn("n4");
        when(n4.getParent()).thenReturn(Optional.empty());
    }

    @Test
    public void testTraversePreOrder() {
        final List<HippoNode> ancestors = traverser.preOrderTraversal(n0).collect(toList());
        assertThat(ancestors, is(asList(n0, n1, n2, n3, n4)));
    }

    @Test
    public void testTraversePostOrder() {
        final List<HippoNode> ancestors = traverser.postOrderTraversal(n0).collect(toList());
        assertThat(ancestors, is(asList(n4, n3, n2, n1, n0)));
    }

    @Test
    public void testTraverseBreadthFirst() {
        final List<HippoNode> ancestors = traverser.breadthFirstTraversal(n0).collect(toList());
        assertThat(ancestors, is(asList(n0, n1, n2, n3, n4)));
    }

}
