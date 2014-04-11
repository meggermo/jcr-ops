package nl.meg.jcr.mutation.internal;

import nl.meg.jcr.HippoNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepostionNodeImplTest {

    @Mock
    private HippoNode n0, n1, n2, n3, parent;

    @Mock
    private Node p;

    @Test
    public void testApplyMoveBefore() throws RepositoryException {
        when(n1.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodeStream()).thenReturn(Arrays.asList(n0, n1, n2, n3).stream());
        when(n1.isSame(n1)).thenReturn(true);
        when(n0.getName()).thenReturn("n0");
        when(n1.getName()).thenReturn("n1");
        when(parent.get()).thenReturn(p);
        final RepostionNodeImpl repostionNode = new RepostionNodeImpl(0);
        repostionNode.apply(n1);
        verify(p).orderBefore("n1", "n0");
    }

    @Test
    public void testApplyMoveAfter() throws RepositoryException {
        when(n1.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodeStream()).thenReturn(Arrays.asList(n0, n1, n2, n3).stream());
        when(n1.isSame(n1)).thenReturn(true);
        when(n1.getName()).thenReturn("n1");
        when(n3.getName()).thenReturn("n3");
        when(parent.get()).thenReturn(p);
        final RepostionNodeImpl repostionNode = new RepostionNodeImpl(2);
        repostionNode.apply(n1);
        verify(p).orderBefore("n1", "n3");
    }

    @Test
    public void testApplyMoveToCurrentPosition() throws RepositoryException {
        when(n1.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodeStream()).thenReturn(Arrays.asList(n0, n1, n2, n3).stream());
        when(n1.isSame(n1)).thenReturn(true);
        when(n1.getName()).thenReturn("n1");
        when(parent.get()).thenReturn(p);
        final RepostionNodeImpl repostionNode = new RepostionNodeImpl(1);
        repostionNode.apply(n1);
        verify(p).orderBefore("n1", "n1");
    }

    @Test
    public void testApplyMoveToEnd() throws RepositoryException {
        when(n1.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodeStream()).thenReturn(Arrays.asList(n0, n1, n2, n3).stream());
        when(n1.isSame(n1)).thenReturn(true);
        when(n1.getName()).thenReturn("n1");
        when(parent.get()).thenReturn(p);
        final RepostionNodeImpl repostionNode = new RepostionNodeImpl(3);
        repostionNode.apply(n1);
        verify(p).orderBefore("n1", null);
    }

}
