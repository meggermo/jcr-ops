package nl.meg.jcr.validation.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.jcr.validation.NodeValidators;
import nl.meg.validation.Error;
import nl.meg.validation.ValidationContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.nodetype.NodeType;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static nl.meg.jcr.validation.NodeErrorCode.*;
import static org.mockito.Mockito.*;

public class NodeValidatorsImplTest extends AbstractMockitoTest {

    private NodeValidators nodeValidators;

    @Mock
    private HippoNode iNode, parent;

    @Mock
    private ValidationContext context;

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
        final Error error = forCode(NODE_CANNOT_BE_ROOT);
        verify(context).addError(eq(IsNotRootValidator.class.getName()),eq(error));
    }

    @Test
    public void testCanAddChildInValid() {
        when(parent.getPrimaryNodeType()).thenReturn(nodeType);
        when(iNode.getName()).thenReturn("name");
        when(iNode.getPrimaryNodeType()).thenReturn(nodeType);
        when(nodeType.canAddChildNode(iNode.getName(), iNode.getPrimaryNodeType().getName())).thenReturn(false);
        nodeValidators.canAddChild(parent).validate(iNode, context);
        final Error error = forCode(CANNOT_ADD_CHILD);
        verify(context).addError(eq(CanAddChildValidator.class.getName()), eq(error));
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
        final Error error = forCode(ITEM_EXISTS);
        verify(context).addError(eq(CanRenameToValidator.class.getName()), eq(error));
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
        final Error error = forCode(ORDERING_NOT_SUPPORTED);
        verify(context).addError(eq(SupportsOrderingValidator.class.getName()), eq(error));
    }

    @Test
    public void testPositionInBounds() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodes()).thenReturn(Stream.of(iNode));
        nodeValidators.positionInBounds(0).validate(iNode, context);
        verifyZeroInteractions(context);
    }

    @Test
    public void testPositionInBounds_ValidationError() {
        when(iNode.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodes()).thenReturn(Stream.of(iNode), Stream.of(iNode));
        nodeValidators.positionInBounds(2).validate(iNode, context);
        final Error error = forCode(POSITION_OUT_OF_RANGE);
        verify(context).addError(eq(PositionInBoundsValidator.class.getName()), eq(error));
    }

    private Error forCode(final NodeErrorCode code) {
        return new Error() {
            @Override
            public String getCode() {
                return code.toString();
            }
            @Override
            public String getMessage() {
                return null;
            }
            @Override
            public Map<String, ?> getParameters() {
                return null;
            }
            @Override
            public boolean equals(Object o) {
                return getCode().equals(((Error)o).getCode());
            }
        };
    }

}
