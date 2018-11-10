package nl.meg.validation.impl;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;
import org.mockito.Mock;

import nl.meg.AbstractMockitoTest;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;
import static java.util.Arrays.asList;
import static nl.meg.validation.impl.ValidatorBuilderImpl.CompositeValidator;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CompositeValidatorImplTest extends AbstractMockitoTest {

    private enum E {}

    @Mock
    private Validator<String> v0, v1;

    @Mock
    private Predicate<ValidationContext> p;

    @Mock
    private ValidationContext c0, c1, c2;

    @Test
    public void testValidate() {
        when(v0.validate("TEST", c0)).thenReturn(c1);
        when(v1.validate("TEST", c1)).thenReturn(c2);
        assertThat(new CompositeValidator<>(asList(v0, v1),p).validate("TEST", c0), is(c2));
    }

    @Test
    public void testValidate_WithInvalidInitialContext() {
        when(p.test(c0)).thenReturn(true);
        assertThat(new CompositeValidator<>(asList(v0, v1), p).validate("TEST", c0), is(c0));
        verifyZeroInteractions(v0, v1);
    }

    @Test
    public void testValidate_ContinueEvenWithErrors() {
        when(p.test(any(ValidationContext.class))).thenReturn(false);
        when(v0.validate("TEST", c0)).thenReturn(c1);
        when(v1.validate("TEST", c1)).thenReturn(c2);
        assertThat(new CompositeValidator<>(asList(v0, v1), p).validate("TEST", c0), is(c2));
    }

    @Test
    public void testValidate_WithoutValidators() {
        when(p.test(any(ValidationContext.class))).thenReturn(true);
        final List<Validator<String>> validators = Collections.emptyList();
        assertThat(new CompositeValidator<>(validators, p).validate("TEST", c0), is(c0));
    }
}
