package nl.meg.jcr.function;

import javax.jcr.RepositoryException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JcrEitherTest {

    @Test
    void testAsLeft() {
        final JcrEither<String, ?> left = JcrEither.left("X");
        assertThat(left.fromLeft())
                .isEqualTo("X");
        assertThatThrownBy(left::fromRight)
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void testAsRight() {
        final JcrEither<Object, String> right = JcrEither.right("X");
        assertThat(right.fromRight())
                .isEqualTo("X");
        assertThatThrownBy(right::fromLeft)
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void testIsLeft() {
        assertThat(JcrEither.left("X").isLeft()).isTrue();
        assertThat(JcrEither.left("X").isRight()).isFalse();
    }

    @Test
    void testIsRight() {
        assertThat(JcrEither.right("X").isLeft()).isFalse();
        assertThat(JcrEither.right("X").isRight()).isTrue();
    }

    @Test
    void testEither() throws RepositoryException {
        final String left = JcrEither.left("X").either(
                l -> l,
                r -> null
        );
        assertThat(left).isNotNull();
        final String right = JcrEither.right("X").either(
                l -> null,
                r -> r
        );
        assertThat(right).isNotNull();
    }

    @Test
    void testEitherAccept() {
        JcrEither.left("X").eitherAccept(
                l -> assertThat(l).isNotNull(),
                r -> Assertions.fail("Did not expect this: %s", r)
        );
        JcrEither.right("X").eitherAccept(
                l -> Assertions.fail("Did not expect this: %s", l),
                r -> assertThat(r).isNotNull()
        );
    }

    @Test
    void testMap() throws RepositoryException {
        assertThat(swap(JcrEither.left("X")).isRight()).isTrue();
        assertThat(swap(JcrEither.right("X")).isLeft()).isTrue();
    }

    private <L, R> JcrEither<R, L> swap(JcrEither<L, R> either) throws RepositoryException {
        return either.map(e -> e.either(JcrEither::right, JcrEither::left));
    }
}
