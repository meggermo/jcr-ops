package nl.meg.jcr.mutation.internal;

import com.google.common.base.Optional;
import nl.meg.jcr.INode;
import nl.meg.jcr.mutation.NodeMethods;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeMethodsImplTest {

    private NodeMethods nodeMethods;

    @Mock
    private INode node, parent;

    @Mock
    private Session session;

    @Mock
    private NodeType nodeType;

    @Mock
    private Node n;

    @Before
    public void setUp() {
        this.nodeMethods = new NodeMethodsImpl();
    }

    @Test
    public void testMove() {
        when(node.getSession()).thenReturn(session);
        nodeMethods.move(node, parent);
    }



    @Test
    public void testRename() {
        when(node.getParent()).thenReturn(Optional.of(parent));
        when(parent.getPrimaryNodeType()).thenReturn(nodeType);
        when(node.getSession()).thenReturn(session);
        nodeMethods.rename(node, "newName");
    }

    @Test
    public void testRenameWithReordering() {
        when(node.getParent()).thenReturn(Optional.of(parent));
        when(parent.getPrimaryNodeType()).thenReturn(nodeType);
        when(nodeType.hasOrderableChildNodes()).thenReturn(true);
        when(parent.getNodes()).thenReturn(Arrays.asList(node, node).iterator());
        when(node.getName()).thenReturn("name");
        when(node.getSession()).thenReturn(session);
        when(parent.get()).thenReturn(n);
        nodeMethods.rename(node, "newName");
    }
}
