package nl.meg.jcr.function;

import java.util.function.Consumer;

import javax.jcr.RepositoryException;

public sealed interface JcrEither<L, R> {

    static <L, R> JcrEither<L, R> left(L value) {
        return new Left<>(value);
    }

    static <L, R> JcrEither<L, R> right(R value) {
        return new Right<>(value);
    }

    boolean isLeft();

    L fromLeft();

    boolean isRight();

    R fromRight();

    default <X> X either(JcrFunction<L, X> f, JcrFunction<R, X> g) throws RepositoryException {
        if (isLeft()) {
            return f.apply(fromLeft());
        } else {
            return g.apply(fromRight());
        }
    }

    default void eitherAccept(Consumer<L> f, Consumer<R> g) {
        if (isLeft()) {
            f.accept(fromLeft());
        } else {
            g.accept(fromRight());
        }
    }


    default <L2, R2> JcrEither<L2, R2> map(JcrFunction<JcrEither<L, R>, JcrEither<L2, R2>> f) throws RepositoryException {
        return f.apply(this);
    }

}

record Left<L, R>(L value) implements JcrEither<L, R> {

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public L fromLeft() {
        return value;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public R fromRight() {
        throw new UnsupportedOperationException("left has no right value");
    }
}

record Right<L, R>(R value) implements JcrEither<L, R> {

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public L fromLeft() {
        throw new UnsupportedOperationException("right has no left value");
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public R fromRight() {
        return value;
    }
}

