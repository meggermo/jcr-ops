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

import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class INodeValidatorsImplTest {

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
    public void testCanAddChild() {
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
        when(parent.hasNode("name")).thenReturn(true);
        when(parent.getPath()).thenReturn("path");
        iNodeValidators.canRenameTo("name").validate(iNode,context);
        verify(context).addError(eq(NodeErrorCode.ITEM_EXISTS), anyMapOf(String.class, Object.class));
    }
}
