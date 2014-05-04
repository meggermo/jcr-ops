package nl.meg.jcr.validation.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.jcr.validation.NodeValidators;
import nl.meg.validation.ValidationContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.nodetype.NodeType;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

public class NodeValidatorsImplTest extends AbstractMockitoTest {

    private NodeValidators nodeValidators;

    @Mock
    private HippoNode iNode, parent;

    @Mock
    private ValidationContext<NodeErrorCode, HippoNode> context;

    @Mock
    private NodeType nodeType;

    @Before
    public void setUp() {
        this.nodeValidators = new NodeValidatorsImpl();
    }

    @Test
    public void testIsNotRoot() {
        nodeValidators.isNotRoot().validate(iNode, context);
    }

    @Test
    public void testIsNotRoot_ValidationError() {
        when(iNode.isRoot()).thenReturn(true);
        nodeValidators.isNotRoot().validate(iNode, context);
        verify(context).addError(eq(NodeErrorCode.NODE_CANNOT_BE_ROOT), anyMapOf(String.class, Object.class));
    }

    @Test
    public void testCanAddChildInValid() {
        when(parent.getPrimaryNodeType()).thenReturn(nodeType);
        when(iNode.getName()).thenReturn("name");
        when(iNode.getPrimaryNodeType()).thenReturn(nodeType);
        when(nodeType.canAddChildNode(iNode.getName(), iNode.getPrimaryNodeType().getName())).thenReturn(false);
        nodeValidators.canAddChild(parent).validate(iNode, context);
        verify(context).addError(eq(NodeErrorCode.CANNOT_ADD_CHILD), anyMapOf(String.class, Object.class));
    }

    @Test
    public void testCanRenameTo() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNode("name")).thenReturn(Optional.<HippoNode>empty());
        nodeValidators.canRenameTo("name").validate(iNode, context);
        verifyZeroInteractions(context);
    }

    @Test
    public void testCanRenameTo_ValidationError() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNode("name")).thenReturn(Optional.of(parent));
        when(parent.getPath()).thenReturn("path");
        nodeValidators.canRenameTo("name").validate(iNode, context);
        verify(context).addError(eq(NodeErrorCode.ITEM_EXISTS), anyMapOf(String.class, Object.class));
    }

    @Test
    public void testSupportsOrdering() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getPrimaryNodeType()).thenReturn(nodeType);
        when(nodeType.hasOrderableChildNodes()).thenReturn(true);
        nodeValidators.supportsOrdering().validate(iNode, context);
        verifyZeroInteractions(context);
    }

    @Test
    public void testSupportsOrdering_ValidationError() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getPrimaryNodeType()).thenReturn(nodeType);
        when(nodeType.hasOrderableChildNodes()).thenReturn(false);
        nodeValidators.supportsOrdering().validate(iNode, context);
        verify(context).addError(eq(NodeErrorCode.ORDERING_NOT_SUPPORTED), anyMapOf(String.class, Object.class));
    }

    @Test
    public void testPositionInBounds() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodesAsStream()).thenReturn(Stream.of(iNode));
        nodeValidators.positionInBounds(0).validate(iNode, context);
        verifyZeroInteractions(context);
    }

    @Test
    public void testPositionInBounds_ValidationError() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodesAsStream()).thenReturn(Stream.of(iNode), Stream.of(iNode));
        nodeValidators.positionInBounds(2).validate(iNode, context);
        verify(context).addError(eq(NodeErrorCode.POSITION_OUT_OF_RANGE), anyMapOf(String.class, Object.class));
    }

}
