package nl.meg.jcr.validation.internal;

import nl.meg.jcr.validation.NodeErrorCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
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
