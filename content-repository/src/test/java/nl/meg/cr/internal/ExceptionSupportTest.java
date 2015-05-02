package nl.meg.cr.internal;

import nl.meg.cr.RepositoryException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class ExceptionSupportTest extends AbstractMockitoTest {

    @Test
    public void test_exception_wrapping() {
        try {
            new ExceptionSupport().tryInvoke(this::toLong, "test", RepositoryException::new);
        } catch (RepositoryException e) {
            assertThat(e.getMessage(), containsString("test is not a number"));
        }
    }

    private Long toLong(String s) throws javax.jcr.RepositoryException {
        throw new javax.jcr.RepositoryException(s + " is not a number");
    }
}
