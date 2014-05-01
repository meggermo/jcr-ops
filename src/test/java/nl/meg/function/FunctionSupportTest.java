package nl.meg.function;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.RepositoryException;
import java.math.BigDecimal;
import java.util.function.Function;

import static nl.meg.function.FunctionSupport.relax;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FunctionSupportTest extends AbstractMockitoTest {

    private enum E {

    }

    @Mock
    private Function<String, Integer> function;

    @Test
    public void testRelax() {
        EFunction<? super Number, BigDecimal, RepositoryException> f = input -> {
            if (input.intValue() == 0) {
                throw new ItemNotFoundException("x");
            }
            if (input.intValue() == 1) {
                throw new ItemExistsException("x");
            }
            return BigDecimal.ONE;
        };
        Function<RepositoryException, ? extends RuntimeException> g = e -> {
            if (ItemExistsException.class.isInstance(e)) {
                return new RuntimeRepositoryException(e) {
                    @Override
                    public String getMessage() {
                        return "value was 0";
                    }
                };
            }
            return new RuntimeRepositoryException(e);
        };
        try {
            relax(f, 0.0D, g);
        } catch (RuntimeException e) {
            assertThat(ItemNotFoundException.class.isInstance(e.getCause()), is(true));
        }
        try {
            relax(f, 1L, g);
        } catch (RuntimeException e) {
            assertThat(ItemExistsException.class.isInstance(e.getCause()), is(true));
            assertThat(e.getMessage(), is("value was 0"));
        }
        assertThat(relax(f, 2, g), is(BigDecimal.ONE));
    }

}
