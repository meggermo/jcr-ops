package nl.meg.jcr.function;

import java.time.Instant;

import javax.jcr.RepositoryException;

import org.junit.jupiter.api.Test;

import static nl.meg.jcr.function.JcrResult.startWith;
import static org.assertj.core.api.Assertions.assertThat;


class JcrResultTest {

    @Test
    void testResultApi() {

        final Instant context = Instant.ofEpochMilli(0);
        final JcrResult<Instant, Boolean> start = startWith(context, i -> i.isAfter(i));
        assertThat(start.getState().fromRight()).isFalse();

        final JcrResult<Instant, Boolean> then1 = start
                .andThen((i, b) -> !b);
        assertThat(then1.getState().fromRight()).isTrue();

        final JcrResult<String, Boolean> switched1 = then1
                .switchContext(b -> Boolean.toString(b), c -> {
                });
        assertThat(then1.getState().fromRight()).isTrue();

        final JcrResult<String, Integer> then2 = switched1
                .andThen((s, b) -> s.length());
        assertThat(then2.getState().fromRight()).isEqualTo(Boolean.TRUE.toString().length());

        final JcrResult<String, Long> then3 = then2
                .andThen((s, i) -> 2L * i)
                .andCall(System.out::println);

        final JcrResult<?, Long> result = then3
                .closeContext(s -> {
                });
        assertThat(result.getState().fromRight()).isEqualTo(8L);
    }

    @Test
    void testResultErrorInNextState() {

        final Instant context = Instant.ofEpochMilli(0);
        final JcrResult<?, Long> result = startWith(context, i -> i.isAfter(i))
                .andThen((i, b) -> {
                    if (!b) {
                        throw new RepositoryException("");
                    }
                    return "X";
                })
                .switchContext(JcrFunction.identity())
                .andThen(String::length)
                .andThen((s, i) -> 2L * i)
                .andCall(System.out::println)
                .closeContext();
        assertThat(result.getState().isLeft()).isTrue();
        assertThat(result.getState().fromLeft()).isInstanceOf(RepositoryException.class);
    }


}
