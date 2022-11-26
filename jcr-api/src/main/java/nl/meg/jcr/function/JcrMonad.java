package nl.meg.jcr.function;

import java.util.Objects;
import java.util.function.Consumer;

import javax.jcr.RepositoryException;

import static nl.meg.jcr.function.JcrEither.left;
import static nl.meg.jcr.function.JcrEither.right;

public final class JcrMonad<C, R> {

    public static <C, R> JcrMonad<C, R> startWith(C context, JcrFunction<C, R> f) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(f);
        return new JcrMonad<>(context, right(context)).andThen((c0, c1) -> f.apply(c0));
    }

    private final C context;

    private final JcrEither<RepositoryException, R> state;

    private JcrMonad(C context, JcrEither<RepositoryException, R> state) {
        this.context = context;
        this.state = Objects.requireNonNull(state);
    }

    public JcrEither<RepositoryException, R> getState() {
        return state;
    }

    public <R2> JcrMonad<C, R2> andThen(JcrBiFunction<C, R, R2> f) {
        return nextResult(f);
    }

    public <R2> JcrMonad<C, R2> andThen(JcrFunction<C, R2> f) {
        return andThen((c, r) -> f.apply(c));
    }

    public JcrMonad<C, R> andCall(JcrConsumer<C> f) {
        return andThen(f.consumeFirst());
    }

    public <C2> JcrMonad<C2, R> switchContext(JcrBiFunction<C, R, C2> f, Consumer<C> contextCloser) {
        return nextContext(f, contextCloser);
    }

    public <C2> JcrMonad<C2, R> switchContext(JcrBiFunction<C, R, C2> f) {
        return nextContext(f, ignore -> {
        });
    }

    public <C2> JcrMonad<C2, R> switchContext(JcrFunction<R, C2> f, Consumer<C> contextCloser) {
        return switchContext((c, r) -> f.apply(r), contextCloser);
    }

    public <C2> JcrMonad<C2, R> switchContext(JcrFunction<R, C2> f) {
        return switchContext((c, r) -> f.apply(r), ignore -> {
        });
    }

    public JcrMonad<Void, R> closeContext(Consumer<C> contextCloser) {
        return switchContext(r -> null, contextCloser);
    }

    public JcrMonad<Void, R> closeContext() {
        return switchContext(r -> null, ignore -> {
        });
    }


    private <R2> JcrMonad<C, R2> nextResult(final JcrBiFunction<C, R, R2> f) {
        return new JcrMonad<>(context, nextState(f));
    }

    private <C2> JcrMonad<C2, R> nextContext(JcrBiFunction<C, R, C2> f, Consumer<C> contextCloser) {
        try {
            return nextState(f).either(
                    e -> new JcrMonad<>(null, left(e)),
                    c -> new JcrMonad<>(c, state)
            );
        } catch (RepositoryException e) {
            return new JcrMonad<>(null, left(e));
        } finally {
            contextCloser.accept(context);
        }
    }

    private <R2> JcrEither<RepositoryException, R2> nextState(JcrBiFunction<C, R, R2> f) {
        try {
            return state.either(JcrEither::left, r -> right(f.apply(context, r)));
        } catch (RepositoryException e) {
            return left(e);
        }
    }
}
