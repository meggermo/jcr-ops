package nl.meg.validation;

import com.google.common.base.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompositeValidatorImplTest {

    enum E {}

    private Validator<E,String> validator;

    @Mock
    private Validator<E,String> v1, v2;

    @Mock
    private Predicate<ValidationContext<E,String>> p;

    @Mock
    private ValidationContext<E,String> context;

    @Before
    public void setUp() {
        validator = new CompositeValidatorImpl<>(asList(v1, v2));
    }

    @Test
    public void testValidate() {
        when(v1.validate("TEST", context)).thenReturn(context);
        when(v2.validate("TEST", context)).thenReturn(context);
        when(context.isValid()).thenReturn(true, false);
        assertThat(validator.validate("TEST", context), is(context));
    }
}
