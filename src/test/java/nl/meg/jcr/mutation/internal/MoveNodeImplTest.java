package nl.meg.jcr.mutation.internal;

import nl.meg.jcr.HippoNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoveNodeImplTest {

    private MoveNodeImpl moveNode;

    @Mock
    private HippoNode node, parent;

    @Mock
    private Session session;

    @Mock
    private NodeType nodeType;

    @Mock
    private Node n;

    @Before
    public void setUp() {
        this.moveNode = new MoveNodeImpl(parent);
    }

    @Test
    public void testMove() {
        when(node.getSession()).thenReturn(session);
        assertThat(moveNode.apply(node), is(node));
    }
}
