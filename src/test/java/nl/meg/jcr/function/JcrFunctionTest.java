package nl.meg.jcr.function;

import javax.jcr.RepositoryException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class JcrFunctionTest {

    @Test
    void testCompose() throws RepositoryException {
        final JcrFunction<Integer, Boolean> f0 = i -> i == 0;
        final JcrFunction<Boolean, Integer> f1 = b -> b ? 1 : 0;
        final JcrFunction<Integer, Integer> composed = f1.compose(f0);
        assertThat(composed.apply(0)).isEqualTo(1);
    }

    @Test
    void testAndThen() throws RepositoryException {
        final JcrFunction<Integer, Boolean> f0 = i -> i == 0;
        final JcrFunction<Boolean, Integer> f1 = b -> b ? 1 : 0;
        final JcrFunction<Integer, Integer> andThen = f0.andThen(f1);
        assertThat(andThen.apply(0)).isEqualTo(1);
    }

    @Test
    void testIdentity() throws RepositoryException {
        assertThat(JcrFunction.identity().apply(1)).isEqualTo(1);
    }
}
