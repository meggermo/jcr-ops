package nl.meg.function.internal;

import nl.meg.function.ValidatingFunctionAdapter;
import nl.meg.function.ValidationException;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidatingFunctionAdapterImplTest {

    private enum E {

    }

    private ValidatingFunctionAdapter<E, String, Integer> adapter;

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
        this.adapter = new ValidatingFunctionAdapterImpl<>(contextSupplier);
    }

    @Test
    public void testApply() {
        when(contextSupplier.get()).thenReturn(context);
        when(validator.validate("TEST", context)).thenReturn(context);
        when(context.isValid()).thenReturn(true);
        adapter.adapt(validator, function).apply("TEST");
    }

    @Test
    public void testApply_ThrowsValidationException() {
        when(contextSupplier.get()).thenReturn(context);
        when(validator.validate("TEST", context)).thenReturn(context);
        when(context.isValid()).thenReturn(false);

        final EnumMap<E, List<Map<String, ?>>> errors = new EnumMap<>(E.class);
        when(context.getErrors()).thenReturn(errors);

        try {
            adapter.adapt(validator, function).apply("TEST");
        } catch (ValidationException e) {
            assertEquals(errors, e.getErrors());
        }
    }

    @Test
    public void testValidate() {
        when(contextSupplier.get()).thenReturn(context);
        when(validator.validate("TEST", context)).thenReturn(context);
        when(context.isValid()).thenReturn(false);
        assertThat(adapter.adapt(validator, function).validate("TEST").isValid(), is(false));
    }

}
