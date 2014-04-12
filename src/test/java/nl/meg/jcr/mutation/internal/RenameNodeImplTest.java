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
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RenameNodeImplTest {

    private RenameNodeImpl renameNode;

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
        this.renameNode = new RenameNodeImpl("newName");
    }

    @Test
    public void testRename() {
        when(node.getParent()).thenReturn(Optional.of(parent));
        when(parent.getPrimaryNodeType()).thenReturn(nodeType);
        when(node.getSession()).thenReturn(session);
        assertThat(renameNode.apply(node), is(node));
    }

    @Test
    public void testRenameWithReordering() {
        when(node.getParent()).thenReturn(Optional.of(parent));
        when(parent.getPrimaryNodeType()).thenReturn(nodeType);
        when(nodeType.hasOrderableChildNodes()).thenReturn(true);
        when(parent.getNodes()).thenReturn(Arrays.asList(node, node));
        when(node.getName()).thenReturn("name");
        when(node.getSession()).thenReturn(session);
        when(parent.get()).thenReturn(n);
        assertThat(renameNode.apply(node), is(node));
    }
}
