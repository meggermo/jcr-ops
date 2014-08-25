package nl.meg.validation.impl;

import nl.meg.AbstractMockitoTest;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.ValidationException;
import nl.meg.validation.Validator;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static nl.meg.validation.impl.ValidatingFunctionSupport.postValidate;
import static nl.meg.validation.impl.ValidatingFunctionSupport.preValidate;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ValidatingFunctionSupportTest extends AbstractMockitoTest {

    @Mock
    private Validator<String> preValidator;

    @Mock
    private ValidationContext preContext;

    @Mock
    private Validator<Integer> postValidator;

    @Mock
    private ValidationContext postContext;

    @Mock
    private Function<String, Integer> function;

    @Test
    public void testPreValidate() {
        when(preValidator.validate(eq("TEST"), any(ValidationContext.class))).thenReturn(preContext);
        when(preContext.hasErrors()).thenReturn(false);
        preValidate(preValidator, function).apply("TEST");
        verify(function).apply("TEST");
    }

    @Test
    public void testPreValidate_ThrowsValidationException() {
        when(preValidator.validate(eq("TEST"), any(ValidationContext.class))).thenReturn(preContext);
        when(preContext.hasErrors()).thenReturn(true);

        final Map<String, nl.meg.validation.Error> errors = new HashMap<>();
        when(preContext.getErrorMap()).thenReturn(errors);

        try {
            preValidate(preValidator, function).apply("TEST");
            shouldHaveThrown(PreValidationException.class);
        } catch (ValidationException e) {
            assertSame(errors, e.getErrorMap());
        }
    }

    @Test
    public void testPostValidate_ThrowsValidationException() {
        when(function.apply("TEST")).thenReturn(1);
        when(postValidator.validate(eq(1), any(ValidationContext.class))).thenReturn(postContext);
        when(postContext.hasErrors()).thenReturn(true);

        final Map<String, nl.meg.validation.Error> errors = new HashMap<>();
        when(postContext.getErrorMap()).thenReturn(errors);

        try {
            postValidate(function, postValidator).apply("TEST");
            shouldHaveThrown(PostValidationException.class);
        } catch (ValidationException e) {
            assertSame(errors, e.getErrorMap());
        }
    }

    @Test
    public void testValidate() {
        when(preValidator.validate(eq("TEST"), any(ValidationContext.class))).thenReturn(preContext);
        when(preContext.hasErrors()).thenReturn(false);
        when(function.apply("TEST")).thenReturn(0);
        assertThat(preValidate(preValidator, function).apply("TEST"), is(0));
    }

    @Test
    public void testPreAndPostValidate() {
        when(preValidator.validate(eq("TEST"), any(ValidationContext.class))).thenReturn(preContext);
        when(preContext.hasErrors()).thenReturn(false);
        when(postValidator.validate(eq(0), any(ValidationContext.class))).thenReturn(postContext);
        when(postContext.hasErrors()).thenReturn(false);
        when(function.apply("TEST")).thenReturn(0);
        final Function<String, Integer> f1 = preValidate(preValidator, function);
        final Function<String, Integer> f2 = postValidate(f1, postValidator);
        assertThat(f2.apply("TEST"), is(0));
    }

}
