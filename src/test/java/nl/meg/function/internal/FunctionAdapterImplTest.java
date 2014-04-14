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
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FunctionAdapterImplTest extends AbstractMockitoTest {

    private enum E {

    }

    private FunctionAdapter<E, String, Integer> adapter;

    @Mock
    private Supplier<ValidationContext<E, String>> contextSupplier;

    @Mock
    private ValidationContext<E, String> context;

    @Mock
    private Validator<E, String> validator;

    @Mock
    private Function<String, Integer> function;

    @Before
    public void setUp() {
        this.adapter = new FunctionAdapterImpl<>(contextSupplier);
    }

    @Test
    public void testApply() {
        when(contextSupplier.get()).thenReturn(context);
        when(validator.validate("TEST", context)).thenReturn(context);
        when(context.isValid()).thenReturn(true);
        adapter.preValidate(validator, function).apply("TEST");
        verify(function).apply("TEST");
    }

    @Test
    public void testApply_ThrowsValidationException() {
        when(contextSupplier.get()).thenReturn(context);
        when(validator.validate("TEST", context)).thenReturn(context);
        when(context.isValid()).thenReturn(false);

        final EnumMap<E, List<Map<String, ?>>> errors = new EnumMap<>(E.class);
        when(context.getErrors()).thenReturn(errors);

        try {
            adapter.preValidate(validator, function).apply("TEST");
        } catch (ValidationException e) {
            assertEquals(errors, e.getErrors());
        }
    }

    @Test
    public void testValidate() {
        when(contextSupplier.get()).thenReturn(context);
        when(validator.validate("TEST", context)).thenReturn(context);
        when(context.isValid()).thenReturn(true);
        when(function.apply("TEST")).thenReturn(0);
        assertThat(adapter.preValidate(validator, function).apply("TEST"), is(0));
    }

}
