package nl.meg.function;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.RepositoryException;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static nl.meg.function.FunctionAdapter.*;
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
        assertThat(f2.apply("TEST"), is(0));
    }

    @Test
    public void testRelax() {
        EFunction<? super Number, BigDecimal, RepositoryException> f = input -> {
            if (input.intValue() == 0) {
                throw new ItemNotFoundException("x");
            }
            if (input.intValue() == 1) {
                throw new ItemExistsException("x");
            }
            return BigDecimal.ONE;
        };
        Function<RepositoryException, ? extends RuntimeException> g = e -> {
            if (ItemExistsException.class.isInstance(e)) {
                return new RuntimeRepositoryException(e) {
                    @Override
                    public String getMessage() {
                        return "value was 0";
                    }
                };
            }
            return new RuntimeRepositoryException(e);
        };
        try {
            relax(f, 0.0D, g);
        } catch (RuntimeException e) {
            assertThat(ItemNotFoundException.class.isInstance(e.getCause()), is(true));
        }
        try {
            relax(f, 1L, g);
        } catch (RuntimeException e) {
            assertThat(ItemExistsException.class.isInstance(e.getCause()), is(true));
            assertThat(e.getMessage(), is("value was 0"));
        }
        assertThat(relax(f, 2, g), is(BigDecimal.ONE));
    }

}
