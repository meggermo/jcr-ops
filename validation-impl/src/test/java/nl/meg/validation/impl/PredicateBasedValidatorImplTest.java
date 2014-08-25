package nl.meg.validation.impl;

import nl.meg.AbstractMockitoTest;
import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;
import org.junit.Test;
import org.mockito.Mock;

import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class PredicateBasedValidatorImplTest extends AbstractMockitoTest {

    @Mock
    private ValidationContext context;

    @Test
    public void testValidate() throws Exception {
        final Validator<Boolean> validator = new PredicateBasedValidatorImpl<Boolean>(Predicate.isEqual(true)) {
            @Override
            protected String getCode() {
                return "code";
            }

            @Override
            protected String getMessage() {
                return "message";
            }
        };
        when(context.hasErrors()).thenReturn(false, true);
        when(context.addError(any(String.class),any(nl.meg.validation.Error.class))).thenReturn(context);
        assertThat(validator.validate(true, context).hasErrors(), is(false));
        assertThat(validator.validate(false, context).hasErrors(), is(true));
    }
}