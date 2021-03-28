package nl.meg.jcr.function;

import java.util.Objects;
import java.util.function.Consumer;

import javax.jcr.RepositoryException;

import static nl.meg.jcr.function.JcrEither.asRight;

public final class JcrResult<C, R> {

    public static <C, R> JcrResult<C, R> startWith(C context, JcrFunction<C, R> f) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(f);
        return new JcrResult<>(context, asRight(context)).andThen((c0, c1) -> f.apply(c0));
    }

    private final C context;

    private final JcrEither<RepositoryException, R> state;

    private JcrResult(C context, JcrEither<RepositoryException, R> state) {
        this.context = context;
        this.state = Objects.requireNonNull(state);
    }

    public JcrEither<RepositoryException, R> getState() {
        return state;
    }

    public <R2> JcrResult<C, R2> andThen(JcrBiFunction<C, R, R2> f) {
        return nextResult(f);
    }

    public <R2> JcrResult<C, R2> andThen(JcrFunction<C, R2> f) {
        return andThen((c, r) -> f.apply(c));
    }

    public JcrResult<C, R> andCall(JcrConsumer<C> f) {
        return andThen(f.consumeFirst());
    }

    public <C2> JcrResult<C2, R> switchContext(JcrBiFunction<C, R, C2> f, Consumer<C> contextCloser) {
        return nextContext(f, contextCloser);
    }

    public <C2> JcrResult<C2, R> switchContext(JcrFunction<R, C2> f, Consumer<C> contextCloser) {
        return switchContext((c, r) -> f.apply(r), contextCloser);
    }

    public <C2> JcrResult<C2, R> switchContext(JcrFunction<R, C2> f) {
        return switchContext((c, r) -> f.apply(r), ignore -> {});
    }

    public JcrResult<Void, R> closeContext(Consumer<C> contextCloser) {
        return switchContext(r -> null, contextCloser);
    }

    public JcrResult<Void, R> closeContext() {
        return switchContext(r -> null, ignore -> {});
    }


    private <R2> JcrResult<C, R2> nextResult(final JcrBiFunction<C, R, R2> f) {
        return new JcrResult<>(context, nextState(f));
    }

    private <C2> JcrResult<C2, R> nextContext(JcrBiFunction<C, R, C2> f, Consumer<C> contextCloser) {
        try {
            return nextState(f).either(
                    e -> new JcrResult<>(null, JcrEither.asLeft(e)),
                    c -> new JcrResult<>(c, state)
            );
        } catch (RepositoryException e) {
            return new JcrResult<>(null, JcrEither.asLeft(e));
        } finally {
            contextCloser.accept(context);
        }
    }

    private <R2> JcrEither<RepositoryException, R2> nextState(JcrBiFunction<C, R, R2> f) {
        try {
            return state.either(JcrEither::asLeft, r -> asRight(f.apply(context, r)));
        } catch (RepositoryException e) {
            return JcrEither.asLeft(e);
        }
    }
}
