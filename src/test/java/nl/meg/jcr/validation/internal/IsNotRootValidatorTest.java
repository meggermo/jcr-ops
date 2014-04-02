package nl.meg.jcr.validation.internal;

import nl.meg.jcr.validation.NodeErrorCode;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IsNotRootValidatorTest {

    private final IsNotRootValidator isNotRootValidator = new IsNotRootValidator();

    @Test
    public void testGetError() {
        assertThat(isNotRootValidator.getError(), is(NodeErrorCode.NODE_CANNOT_BE_ROOT));
    }

}
