package nl.meg.function.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.function.FunctionAdapter;
import nl.meg.function.ValidationException;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FunctionAdapterImplTest extends AbstractMockitoTest {

    private enum E {

    }

    private FunctionAdapter<E, String, Integer> adapter;

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

    @Before
    public void setUp() {
        this.adapter = new FunctionAdapterImpl<>();
    }

    @Test
    public void testPreValidate() {
        when(preValidator.validate(eq("TEST"), any(ValidationContext.class))).thenReturn(preContext);
        when(preContext.isValid()).thenReturn(true);
        adapter.preValidate(preValidator, function).apply("TEST");
        verify(function).apply("TEST");
    }

    @Test
    public void testPreValidate_ThrowsValidationException() {
        when(preValidator.validate(eq("TEST"), any(ValidationContext.class))).thenReturn(preContext);
        when(preContext.isValid()).thenReturn(false);

        final EnumMap<E, List<Map<String, ?>>> errors = new EnumMap<>(E.class);
        when(preContext.getErrors()).thenReturn(errors);

        try {
            adapter.preValidate(preValidator, function).apply("TEST");
        } catch (ValidationException e) {
            assertEquals(errors, e.getErrors());
        }
    }

    @Test
    public void testValidate() {
        when(preValidator.validate(eq("TEST"), any(ValidationContext.class))).thenReturn(preContext);
        when(preContext.isValid()).thenReturn(true);
        when(function.apply("TEST")).thenReturn(0);
        assertThat(adapter.preValidate(preValidator, function).apply("TEST"), is(0));
    }

    @Test
    public void testPreAndPostValidate() {
        when(preValidator.validate(eq("TEST"), any(ValidationContext.class))).thenReturn(preContext);
        when(preContext.isValid()).thenReturn(true);
        when(postValidator.validate(eq(0), any(ValidationContext.class))).thenReturn(postContext);
        when(postContext.isValid()).thenReturn(true);
        when(function.apply("TEST")).thenReturn(0);
        final Function<String, Integer> f1 = adapter.preValidate(preValidator, function);
        final Function<String, Integer> f2 = adapter.postValidate(postValidator, f1);
        assertThat(f1.apply("TEST"), is(0));
    }
}
