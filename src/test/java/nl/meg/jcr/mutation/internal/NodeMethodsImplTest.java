package nl.meg.jcr.mutation.internal;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import nl.meg.function.ValidatingFunction;
import nl.meg.function.ValidatingFunctionAdapter;
import nl.meg.jcr.INode;
import nl.meg.jcr.mutation.NodeMethods;
import nl.meg.jcr.validation.INodeValidators;
import nl.meg.jcr.validation.NodeErrorCode;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Predicate;

import static nl.meg.validation.ValidatorBuilder.builder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeMethodsImplTest {

    private NodeMethods nodeMethods;

    @Mock
    private INode node, parent;

    @Mock
    private INodeValidators nodeValidators;

    @Mock
    private Predicate<ValidationContext<NodeErrorCode, INode>> continueValidation;

    @Mock
    private Supplier<ValidationContext<NodeErrorCode, INode>> contextSupplier;

    @Mock
    private Validator<NodeErrorCode, INode> validator;

    @Mock
    private ValidationContext<NodeErrorCode, INode> context;

    @Mock
    private ValidatingFunctionAdapter<NodeErrorCode, INode, INode> validatingFunctionAdapter;

    @Mock
    private ValidatingFunction<INode, INode> function;

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
