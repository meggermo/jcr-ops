package nl.meg.jcr.function;

import javax.jcr.RepositoryException;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JcrBiFunctionTest {

    @Test
    public void testBindFirst() throws RepositoryException {
        final JcrBiFunction<Integer, Integer, Integer> f = (i, j) -> i + j;
        assertThat(f.bindFirst(1).apply(-1)).isEqualTo(0);
    }

    @Test
    public void testBindSecond() throws RepositoryException {
        final JcrBiFunction<Integer, Integer, Integer> f = (i, j) -> i + j;
        assertThat(f.bindSecond(-1).apply(1)).isEqualTo(0);
    }

    @Test
    public void testAndThen() throws RepositoryException {
        final JcrBiFunction<Integer, Integer, Integer> f = (i, j) -> i + j;
        assertThat(f.andThen(i -> 10 * i).apply(1, 2)).isEqualTo(30);
    }
}
