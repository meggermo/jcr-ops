package nl.meg.jcr.mutation.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.mutation.NodeMethods;
import nl.meg.jcr.validation.NodeValidators;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class NodeMethodsImplTest extends AbstractMockitoTest {

    private NodeMethods nodeMethods;

    @Mock
    private HippoNode node0, parent, node1, node2;

    @Mock
    private NodeValidators nodeValidators;

    @Mock
    private Predicate<ValidationContext> terminationPredicate;

    @Mock
    private Validator<HippoNode> validator;

    @Mock
    private ValidationContext context;

    @Mock
    private Function<HippoNode, HippoNode> function;

    @Mock
    private Session session;

    @Mock
    private NodeType nodeType;

    @Mock
    private Node parentNode;

    @Before
    public void setUp() {
        when(node0.getSession()).thenReturn(session);
        when(node0.getParent()).thenReturn(Optional.of(parent));
        when(nodeValidators.canAddChild(parent)).thenReturn(validator);
        when(nodeValidators.isNotRoot()).thenReturn(validator);
        when(validator.validate(eq(node0),any(ValidationContext.class))).thenReturn(context);
        when(function.apply(node0)).thenReturn(node0);
        this.nodeMethods = new NodeMethodsImpl(nodeValidators, terminationPredicate);
    }

    @Test
    public void testMove() {
        when(context.hasErrors()).thenReturn(false);
        assertThat(nodeMethods.moveFunction(parent).apply(node0), is(node0));
    }

    @Test
    public void testValidateMoveCallsNodeValidators() {
        nodeMethods.moveFunction(parent).apply(node0);
        verify(nodeValidators).isNotRoot();
        verify(nodeValidators).canAddChild(parent);
        verifyNoMoreInteractions(nodeValidators);
    }

    @Test
    public void testRename() {
        when(parent.getPrimaryNodeType()).thenReturn(nodeType);
        when(nodeType.hasOrderableChildNodes()).thenReturn(true);
        when(parent.getNodesAsStream()).thenReturn(Stream.<HippoNode>empty());
        when(nodeValidators.canRenameTo("newName")).thenReturn(validator);
        assertThat(nodeMethods.renameFunction("newName").apply(node0), is(node0));
    }

    @Test
    public void testReposition() throws RepositoryException {
        when(node0.getName()).thenReturn("name");
        when(node1.getName()).thenReturn("node1");
        when(node2.getName()).thenReturn("node2");
        when(parent.getNodesAsStream()).thenReturn(Stream.of(node0, node1, node2));
        when(parent.get()).thenReturn(parentNode);
        when(nodeValidators.supportsOrdering()).thenReturn(validator);
        when(nodeValidators.positionInBounds(1)).thenReturn(validator);
        assertThat(nodeMethods.repositionFunction(1).apply(node0), is(node0));
        verify(parentNode).orderBefore("name","node2");
    }


}
