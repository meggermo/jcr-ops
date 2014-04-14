package nl.meg.function;

import nl.meg.AbstractMockitoTest;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;
import org.junit.Test;
import org.mockito.Mock;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static nl.meg.function.FunctionAdapter.postValidate;
import static nl.meg.function.FunctionAdapter.preValidate;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FunctionAdapterTest extends AbstractMockitoTest {

    private enum E {

    }

    @Mock
    private Validator<E, String> preValidator;

    @Mock
    private ValidationContext<E, String> preContext;

    @Mock
    private Validator<E, Integer> postValidator;

    @Mock
    private ValidationContext<E, Integer> postContext;

    @Mock
    private Function<String, Integer> function;

    @Test
    public void testPreValidate() {
        when(preValidator.validate(eq("TEST"), any(ValidationContext.class))).thenReturn(preContext);
        when(preContext.isValid()).thenReturn(true);
        preValidate(preValidator, function).apply("TEST");
        verify(function).apply("TEST");
    }

    @Test
    public void testPreValidate_ThrowsValidationException() {
        when(preValidator.validate(eq("TEST"), any(ValidationContext.class))).thenReturn(preContext);
        when(preContext.isValid()).thenReturn(false);

        final EnumMap<E, List<Map<String, ?>>> errors = new EnumMap<>(E.class);
        when(preContext.getErrors()).thenReturn(errors);

        try {
            preValidate(preValidator, function).apply("TEST");
        } catch (ValidationException e) {
            assertEquals(errors, e.getErrors());
        }
    }

    @Test
    public void testValidate() {
        when(preValidator.validate(eq("TEST"), any(ValidationContext.class))).thenReturn(preContext);
        when(preContext.isValid()).thenReturn(true);
        when(function.apply("TEST")).thenReturn(0);
        assertThat(preValidate(preValidator, function).apply("TEST"), is(0));
    }

    @Test
    public void testPreAndPostValidate() {
        when(preValidator.validate(eq("TEST"), any(ValidationContext.class))).thenReturn(preContext);
        when(preContext.isValid()).thenReturn(true);
        when(postValidator.validate(eq(0), any(ValidationContext.class))).thenReturn(postContext);
        when(postContext.isValid()).thenReturn(true);
        when(function.apply("TEST")).thenReturn(0);
        final Function<String, Integer> f1 = preValidate(preValidator, function);
        final Function<String, Integer> f2 = postValidate(postValidator, f1);
        assertThat(f1.apply("TEST"), is(0));
    }
}
