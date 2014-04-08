package nl.meg.validation;

import com.google.common.base.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompositeValidatorImplTest {

    private enum E {}

    @Mock
    private Validator<E, String> v0, v1;

    @Mock
    private Predicate<ValidationContext<E, String>> p;

    @Mock
    private ValidationContext<E, String> c0, c1, c2;

    @Test
    public void testValidate() {
        when(c0.isValid()).thenReturn(true);
        when(v0.validate("TEST", c0)).thenReturn(c1);
        when(c1.isValid()).thenReturn(true);
        when(v1.validate("TEST", c1)).thenReturn(c2);
        assertThat(new CompositeValidatorImpl<>(asList(v0, v1)).validate("TEST", c0), is(c2));
    }

    @Test
    public void testValidate_WithInvalidInitialContext() {
        when(c0.isValid()).thenReturn(false);
        assertThat(new CompositeValidatorImpl<>(asList(v0, v1)).validate("TEST", c0), is(c0));
        verifyZeroInteractions(v0, v1);
    }

    @Test
    public void testValidate_ContinueEvenWithErrors() {
        when(p.apply(any(ValidationContext.class))).thenReturn(true);
        when(c0.isValid()).thenReturn(false);
        when(v0.validate("TEST", c0)).thenReturn(c1);
        when(c1.isValid()).thenReturn(false);
        when(v1.validate("TEST", c1)).thenReturn(c2);
        assertThat(new CompositeValidatorImpl<>(asList(v0, v1), p).validate("TEST", c0), is(c2));
    }

    @Test
    public void testValidate_WithoutValidators() {
        when(p.apply(any(ValidationContext.class))).thenReturn(true);
        final List<Validator<E, String>> validators = Collections.emptyList();
        assertThat(new CompositeValidatorImpl<>(validators, p).validate("TEST", c0), is(c0));
    }
}
