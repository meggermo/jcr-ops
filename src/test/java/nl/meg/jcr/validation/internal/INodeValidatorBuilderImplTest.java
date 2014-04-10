package nl.meg.jcr.validation.internal;

import com.google.common.base.Optional;
import nl.meg.jcr.INode;
import nl.meg.jcr.validation.INodeValidators;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.ValidationContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.nodetype.NodeType;

import java.util.Arrays;

import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class INodeValidatorBuilderImplTest {

    private INodeValidators iNodeValidators;

    @Mock
    private INode iNode, parent;

    @Mock
    private ValidationContext<NodeErrorCode, INode> context;

    @Mock
    private NodeType nodeType;

    @Before
    public void setUp() {
        this.iNodeValidators = new INodeValidatorsImpl();
    }

    @Test
    public void testIsNotRoot() {
        iNodeValidators.isNotRoot().validate(iNode, context);
    }

    @Test
    public void testIsNotRoot_ValidationError() {
        when(iNode.isRoot()).thenReturn(true);
        iNodeValidators.isNotRoot().validate(iNode, context);
        verify(context).addError(eq(NodeErrorCode.NODE_CANNOT_BE_ROOT), anyMapOf(String.class, Object.class));
    }

    @Test
    public void testCanAddChildInValid() {
        when(parent.getPrimaryNodeType()).thenReturn(nodeType);
        when(iNode.getName()).thenReturn("name");
        when(iNode.getPrimaryNodeType()).thenReturn(nodeType);
        when(nodeType.canAddChildNode(iNode.getName(), iNode.getPrimaryNodeType().getName())).thenReturn(false);
        iNodeValidators.canAddChild(parent).validate(iNode, context);
        verify(context).addError(eq(NodeErrorCode.CANNOT_ADD_CHILD), anyMapOf(String.class, Object.class));
    }

    @Test
    public void testCanRenameTo() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNode("name")).thenReturn(Optional.<INode>absent());
        iNodeValidators.canRenameTo("name").validate(iNode, context);
        verifyZeroInteractions(context);
    }

    @Test
    public void testCanRenameTo_ValidationError() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNode("name")).thenReturn(Optional.of(parent));
        when(parent.getPath()).thenReturn("path");
        iNodeValidators.canRenameTo("name").validate(iNode, context);
        verify(context).addError(eq(NodeErrorCode.ITEM_EXISTS), anyMapOf(String.class, Object.class));
    }

    @Test
    public void testSupportsOrdering() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getPrimaryNodeType()).thenReturn(nodeType);
        when(nodeType.hasOrderableChildNodes()).thenReturn(true);
        iNodeValidators.supportsOrdering().validate(iNode, context);
        verifyZeroInteractions(context);
    }

    @Test
    public void testSupportsOrdering_ValidationError() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getPrimaryNodeType()).thenReturn(nodeType);
        when(nodeType.hasOrderableChildNodes()).thenReturn(false);
        iNodeValidators.supportsOrdering().validate(iNode, context);
        verify(context).addError(eq(NodeErrorCode.ORDERING_NOT_SUPPORTED), anyMapOf(String.class, Object.class));
    }

    @Test
    public void testPositionInBounds() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodes()).thenReturn(Arrays.asList(iNode).iterator());
        iNodeValidators.positionInBounds(0).validate(iNode, context);
        verifyZeroInteractions(context);
    }

    @Test
    public void testPositionInBounds_ValidationError() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodes()).thenReturn(Arrays.asList(iNode).iterator());
        iNodeValidators.positionInBounds(2).validate(iNode, context);
        verify(context).addError(eq(NodeErrorCode.POSITION_OUT_OF_RANGE), anyMapOf(String.class, Object.class));
    }

}
