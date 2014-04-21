package nl.meg.validation;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValidationContextTest {

    enum E {
        A, B
    }

    private ValidationContext<E,String> context;

    @Before
    public void setUp() {
        this.context = new ValidationContext<>();
    }

    @Test
    public void testIsValid() {
        assertThat(context.isValid(), is(true));
        final Map<String, ?> contextParameterMap = Collections.<String, Object>emptyMap();
        context.addError(E.A, contextParameterMap);
        context.addError(E.A, contextParameterMap);
        assertThat(context.isValid(), is(false));
        assertThat(context.getErrors().get(E.A).size(), is(2));
    }

}
