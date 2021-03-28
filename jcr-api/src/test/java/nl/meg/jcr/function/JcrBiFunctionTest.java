package nl.meg.jcr.function;

import javax.jcr.RepositoryException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JcrBiFunctionTest {

    @Test
    void testBindFirst() throws RepositoryException {
        final JcrBiFunction<Integer, Integer, Integer> f = Integer::sum;
        assertThat(f.bindFirst(1).apply(-1)).isZero();
    }

    @Test
    void testBindSecond() throws RepositoryException {
        final JcrBiFunction<Integer, Integer, Integer> f = Integer::sum;
        assertThat(f.bindSecond(-1).apply(1)).isZero();
    }

    @Test
    void testAndThen() throws RepositoryException {
        final JcrBiFunction<Integer, Integer, Integer> f = Integer::sum;
        assertThat(f.andThen(i -> 10 * i).apply(1, 2)).isEqualTo(30);
    }
}
