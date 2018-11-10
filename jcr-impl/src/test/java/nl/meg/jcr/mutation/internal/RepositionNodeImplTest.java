package nl.meg.jcr.mutation.internal;

import java.util.Optional;
import java.util.stream.Stream;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.junit.Test;
import org.mockito.Mock;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoNode;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepositionNodeImplTest extends AbstractMockitoTest {

    @Mock
    private HippoNode n0, n1, n2, n3, parent;

    @Mock
    private Node p;

    @Test
    public void testApplyMoveBefore() throws RepositoryException {
        when(n1.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodes()).thenReturn(Stream.of(n0, n1, n2, n3));
        when(n0.getName()).thenReturn("n0");
        when(n1.getName()).thenReturn("n1");
        when(parent.get()).thenReturn(p);
        final RepositionNodeImpl repostionNode = new RepositionNodeImpl(0);
        repostionNode.apply(n1);
        verify(p).orderBefore("n1", "n0");
    }

    @Test
    public void testApplyMoveAfter() throws RepositoryException {
        when(n1.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodes()).thenReturn(Stream.of(n0, n1, n2, n3));
        when(n1.getName()).thenReturn("n1");
        when(n3.getName()).thenReturn("n3");
        when(parent.get()).thenReturn(p);
        final RepositionNodeImpl repostionNode = new RepositionNodeImpl(2);
        repostionNode.apply(n1);
        verify(p).orderBefore("n1", "n3");
    }

    @Test
    public void testApplyMoveToCurrentPosition() throws RepositoryException {
        when(n1.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodes()).thenReturn(Stream.of(n0, n1, n2, n3));
        when(n1.getName()).thenReturn("n1");
        when(parent.get()).thenReturn(p);
        final RepositionNodeImpl repostionNode = new RepositionNodeImpl(1);
        repostionNode.apply(n1);
        verify(p).orderBefore("n1", "n1");
    }

    @Test
    public void testApplyMoveToEnd() throws RepositoryException {
        when(n1.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodes()).thenReturn(Stream.of(n0, n1, n2, n3));
        when(n1.getName()).thenReturn("n1");
        when(parent.get()).thenReturn(p);
        final RepositionNodeImpl repostionNode = new RepositionNodeImpl(3);
        repostionNode.apply(n1);
        verify(p).orderBefore("n1", null);
    }

}
