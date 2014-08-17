package nl.meg.validation.impl;

import nl.meg.AbstractMockitoTest;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;
import org.junit.Test;
import org.mockito.Mock;

import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ValidatorBuilderImplTest extends AbstractMockitoTest {

    @Mock
    private Validator<Long> validator;

    @Mock
    private Predicate<ValidationContext> terminate;

    @Mock
    private ValidationContext c1, c2, c3;

    @Test
    public void testAdd() throws Exception {
        when(validator.validate(1L, c1)).thenReturn(c2);
        final Validator<Long> v = new ValidatorBuilderImpl<Long>().add(validator).build(terminate);
        assertThat(v.validate(1L, c1), is(c2));
    }

    @Test
    public void testValidationTerminatesImmediately() throws Exception {
        final Validator<Long> v = new ValidatorBuilderImpl<Long>().add(validator).build(terminate);
        when(terminate.test(c1)).thenReturn(true);
        assertThat(v.validate(1L, c1), is(c1));
        verifyZeroInteractions(validator);
    }

    @Test
    public void testValidationTerminatesAfterSecondValidation() throws Exception {
        final Validator<Long> v = new ValidatorBuilderImpl<Long>()
                        .add(validator)
                        .add(validator)
                        .build(terminate);
        when(validator.validate(1L, c1)).thenReturn(c2);
        when(validator.validate(1L, c2)).thenReturn(c3);
        when(terminate.test(c3)).thenReturn(true);
        assertThat(v.validate(1L, c1), is(c3));
    }

    @Test
    public void testBuild() throws Exception {
        final Validator<String> v = new ValidatorBuilderImpl<String>().build(terminate);
        assertThat(v.validate("test", c1), is(c1));
    }
}