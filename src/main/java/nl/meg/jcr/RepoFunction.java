package nl.meg.jcr;

import javax.jcr.RepositoryException;

public interface RepoFunction<S, T> {

    T apply(S source) throws RepositoryException;
}
