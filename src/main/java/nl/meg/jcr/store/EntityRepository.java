package nl.meg.jcr.store;

import javax.jcr.Credentials;
import javax.jcr.RepositoryException;

import nl.meg.jcr.function.JcrEither;

public interface EntityRepository<E> {

    JcrEither<RepositoryException, E> read(Credentials credentials);

    JcrEither<RepositoryException, E> write(Credentials credentials, E entity);

}
