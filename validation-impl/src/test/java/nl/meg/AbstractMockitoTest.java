package nl.meg;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractMockitoTest {

    protected final <E extends Throwable> void shouldHaveThrown(Class<E> exceptionClass) {
        fail(String.format("Should have thrown a %s", exceptionClass.getSimpleName()));
    }

}
