package nl.meg.jcr.function;

import java.util.function.Consumer;

import javax.jcr.RepositoryException;

public abstract class JcrEither<L, R> {

    public static <L, R> JcrEither<L, R> asLeft(L value) {
        return new Left<>(value);
    }

    public static <L, R> JcrEither<L, R> asRight(R value) {
        return new Right<>(value);
    }

    abstract boolean isLeft();

    abstract L fromLeft();

    abstract boolean isRight();

    abstract R fromRight();

    public final <X> X either(JcrFunction<L, X> f, JcrFunction<R, X> g) throws RepositoryException {
        if (isLeft()) {
            return f.apply(fromLeft());
        } else {
            return g.apply(fromRight());
        }
    }

    public final void eitherAccept(Consumer<L> f, Consumer<R> g) {
        if (isLeft()) {
            f.accept(fromLeft());
        } else {
            g.accept(fromRight());
        }
    }


    public final <L2, R2> JcrEither<L2, R2> map(JcrFunction<JcrEither<L, R>, JcrEither<L2, R2>> f) throws RepositoryException {
        return f.apply(this);
    }


    private static final class Left<L, R> extends JcrEither<L, R> {

        private final L value;

        private Left(final L value) {
            this.value = value;
        }

        @Override
        boolean isLeft() {
            return true;
        }

        @Override
        L fromLeft() {
            return value;
        }

        @Override
        boolean isRight() {
            return false;
        }

        @Override
        R fromRight() {
            throw new UnsupportedOperationException("left has no right value");
        }
    }

    private static final class Right<L, R> extends JcrEither<L, R> {

        private final R value;

        private Right(final R value) {
            this.value = value;
        }

        @Override
        boolean isLeft() {
            return false;
        }

        @Override
        L fromLeft() {
            throw new UnsupportedOperationException("right has no left value");
        }

        @Override
        boolean isRight() {
            return true;
        }

        @Override
        R fromRight() {
            return value;
        }
    }

}
