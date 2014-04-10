package nl.meg.jcr;

import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.RepositoryException;

public final class RepoFunctionInvoker {

    public static <S, T> T invoke(S source, RepoFunction<S, T> repoFunction) {
        try {
            return repoFunction.apply(source);
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }
}
