package nl.meg.jcr.mutation.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.function.ValidatingFunction;
import nl.meg.function.ValidatingFunctionAdapter;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.mutation.NodeMethods;
import nl.meg.jcr.validation.INodeValidators;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static nl.meg.validation.ValidatorBuilder.builder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class NodeMethodsImplTest extends AbstractMockitoTest {

    private NodeMethods nodeMethods;

    @Mock
    private HippoNode node, parent;

    @Mock
    private INodeValidators nodeValidators;

    @Mock
    private Predicate<ValidationContext<NodeErrorCode, HippoNode>> continueValidation;

    @Mock
    private Supplier<ValidationContext<NodeErrorCode, HippoNode>> contextSupplier;

    @Mock
    private Validator<NodeErrorCode, HippoNode> validator;

    @Mock
    private ValidationContext<NodeErrorCode, HippoNode> context;

    @Mock
    private ValidatingFunctionAdapter<NodeErrorCode, HippoNode, HippoNode> validatingFunctionAdapter;

    @Mock
    private ValidatingFunction<HippoNode, HippoNode> function;

    @Before
    public void setUp() {
        when(nodeValidators.canAddChild(parent)).thenReturn(validator);
        when(nodeValidators.isNotRoot()).thenReturn(validator);
        when(contextSupplier.get()).thenReturn(context);
        when(validatingFunctionAdapter.adapt(any(Validator.class), any(Function.class))).thenReturn(function);
        when(function.apply(node)).thenReturn(node);
        this.nodeMethods = new NodeMethodsImpl(nodeValidators, builder(continueValidation), validatingFunctionAdapter);
    }

    @Test
    public void testMove() {
        assertThat(nodeMethods.moveFunction(parent).apply(node), is(node));
    }

    @Test
    public void testValidateMoveCallsNodeValidators() {
        nodeMethods.moveFunction(parent).validate(node);
        verify(nodeValidators).isNotRoot();
        verify(nodeValidators).canAddChild(parent);
        verifyNoMoreInteractions(nodeValidators);
    }

    @Test
    public void testRename() {
        assertThat(nodeMethods.renameFunction("newName").apply(node), is(node));
    }

    @Test
    public void testValidateRenameCallsNodeValidators() {
        nodeMethods.renameFunction("newName").validate(node);
        verify(nodeValidators).isNotRoot();
        verify(nodeValidators).canRenameTo("newName");
        verifyNoMoreInteractions(nodeValidators);
    }

    @Test
    public void testReposition() {
        assertThat(nodeMethods.repositionFunction(0).apply(node), is(node));
    }


}
