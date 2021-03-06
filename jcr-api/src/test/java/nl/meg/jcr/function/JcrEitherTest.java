package nl.meg.jcr.function;

import javax.jcr.RepositoryException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JcrEitherTest {

    @Test
    void testAsLeft() {
        final JcrEither<String, ?> left = JcrEither.asLeft("X");
        assertThat(left.fromLeft())
                .isEqualTo("X");
        assertThatThrownBy(left::fromRight)
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void testAsRight() {
        final JcrEither<Object, String> right = JcrEither.asRight("X");
        assertThat(right.fromRight())
                .isEqualTo("X");
        assertThatThrownBy(right::fromLeft)
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void testIsLeft() {
        assertThat(JcrEither.asLeft("X").isLeft()).isTrue();
        assertThat(JcrEither.asLeft("X").isRight()).isFalse();
    }

    @Test
    void testIsRight() {
        assertThat(JcrEither.asRight("X").isLeft()).isFalse();
        assertThat(JcrEither.asRight("X").isRight()).isTrue();
    }

    @Test
    void testEither() throws RepositoryException {
        final String left = JcrEither.asLeft("X").either(
                l -> l,
                r -> null
        );
        assertThat(left).isNotNull();
        final String right = JcrEither.asRight("X").either(
                l -> null,
                r -> r
        );
        assertThat(right).isNotNull();
    }

    @Test
    void testEitherAccept() {
        JcrEither.asLeft("X").eitherAccept(
                l -> assertThat(l).isNotNull(),
                r -> Assertions.fail("Did not expect this: %s", r)
        );
        JcrEither.asRight("X").eitherAccept(
                l -> Assertions.fail("Did not expect this: %s", l),
                r -> assertThat(r).isNotNull()
        );
    }

    @Test
    void testMap() throws RepositoryException {
        assertThat(swap(JcrEither.asLeft("X")).isRight()).isTrue();
        assertThat(swap(JcrEither.asRight("X")).isLeft()).isTrue();
    }

    private <L, R> JcrEither<R, L> swap(JcrEither<L, R> either) throws RepositoryException {
        return either.map(e -> e.either(JcrEither::asRight, JcrEither::asLeft));
    }
}
