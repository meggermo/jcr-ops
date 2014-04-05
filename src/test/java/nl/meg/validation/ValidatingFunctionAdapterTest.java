package nl.meg.validation;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidatingFunctionAdapterTest {

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
        this.adapter = new ValidatingFunctionAdapter<>(contextSupplier,validator,function);
    }

    @Test
    public void testApply() {
        when(contextSupplier.get()).thenReturn(context);
        when(validator.validate("TEST", context)).thenReturn(context);
        when(context.isValid()).thenReturn(true);
        adapter.apply("TEST");
    }

    @Test
    public void testApply_ThrowsValidationException() {
        when(contextSupplier.get()).thenReturn(context);
        when(validator.validate("TEST", context)).thenReturn(context);
        when(context.isValid()).thenReturn(false);

        final EnumMap<E, List<Map<String, ?>>> errors = new EnumMap<>(E.class);
        when(context.getErrors()).thenReturn(errors);

        try {
            adapter.apply("TEST");
        } catch (ValidationException e) {
            assertEquals(errors, e.getErrors());
        }
    }
}
