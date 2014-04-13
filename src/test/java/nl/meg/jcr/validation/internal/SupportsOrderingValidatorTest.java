package nl.meg.jcr.validation.internal;

import nl.meg.jcr.validation.NodeErrorCode;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SupportsOrderingValidatorTest {

    private SupportsOrderingValidator validator;

    @Before
    public void setUp() {
        this.validator = new SupportsOrderingValidator();
    }

    @Test
    public void testGetError() {
        assertThat(validator.getError(), is(NodeErrorCode.ORDERING_NOT_SUPPORTED));
    }
}
