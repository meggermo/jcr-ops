package nl.meg.validation.impl;

import nl.meg.validation.ValidationContext;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValidationContextImplTest {

    private ValidationContext context;

    @Before
    public void setUp() {
        this.context = new ValidationContextImpl();
    }

    @Test
    public void testIsValid() {
        assertThat(context.hasErrors(), is(false));
        context = context.addError("A", new ErrorImpl("A","error"));
        context = context.addError("B", new ErrorImpl("B","error"));
        assertThat(context.hasErrors(), is(true));
        assertThat(context.getErrorMap().get("A").getCode(), is("A"));
    }

}
