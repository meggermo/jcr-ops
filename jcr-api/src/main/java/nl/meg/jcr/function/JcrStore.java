package nl.meg.jcr.function;

import javax.jcr.Credentials;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public interface JcrStore {

    <R> JcrEither<RepositoryException, R> read(Credentials credentials, JcrFunction<Session, R> task);

    <R> JcrEither<RepositoryException, R> write(Credentials credentials, JcrFunction<Session, R> task);

}
