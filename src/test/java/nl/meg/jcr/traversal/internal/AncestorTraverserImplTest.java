package nl.meg.jcr.traversal.internal;

import com.google.common.base.Optional;
import com.google.common.collect.TreeTraverser;
import nl.meg.jcr.INode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.RepositoryException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AncestorTraverserImplTest {

    private final TreeTraverser<INode> traverser = new AncestorTraverserImpl();

    @Mock
    private INode n0, n1, n2, n3, n4;

    @Mock
    private Optional<INode> on1, on2, on3, on4, on5;

    @Mock
    private RepositoryException e;

    @Before
    public void setUp() throws RepositoryException {

        when(n0.getParent()).thenReturn(on1);
        when(on1.isPresent()).thenReturn(true);
        when(on1.get()).thenReturn(n1);

        when(n1.getName()).thenReturn("n1");
        when(n1.getParent()).thenReturn(on2);
        when(on2.isPresent()).thenReturn(true);
        when(on2.get()).thenReturn(n2);

        when(n2.getName()).thenReturn("n2");
        when(n2.getParent()).thenReturn(on3);
        when(on3.isPresent()).thenReturn(true);
        when(on3.get()).thenReturn(n3);

        when(n3.getName()).thenReturn("n3");
        when(n3.getParent()).thenReturn(on4);
        when(on4.isPresent()).thenReturn(true);
        when(on4.get()).thenReturn(n4);

        when(n4.getName()).thenReturn("n4");
        when(n4.getParent()).thenReturn(on5);
        when(on5.isPresent()).thenReturn(false);
    }

    @Test
    public void testTraversePreOrder() {
        final List<INode> ancestors = traverser.preOrderTraversal(n0).toList();
        assertThat(ancestors, is(asList(n0, n1, n2, n3, n4)));
    }

    @Test
    public void testTraversePostOrder() {
        final List<INode> ancestors = traverser.postOrderTraversal(n0).toList();
        assertThat(ancestors, is(asList(n4, n3, n2, n1, n0)));
    }

    @Test
    public void testTraverseBreadthFirst() {
        final List<INode> ancestors = traverser.breadthFirstTraversal(n0).toList();
        assertThat(ancestors, is(asList(n0, n1, n2, n3, n4)));
    }

}
