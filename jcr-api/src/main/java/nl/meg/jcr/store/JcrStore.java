package nl.meg.jcr.store;

import javax.jcr.Credentials;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import nl.meg.jcr.function.JcrEither;
import nl.meg.jcr.function.JcrFunction;

public interface JcrStore {

    <R> JcrEither<RepositoryException, R> read(Credentials credentials, JcrFunction<Session, R> task);

    <R> JcrEither<RepositoryException, R> write(Credentials credentials, JcrFunction<Session, R> task);

}
