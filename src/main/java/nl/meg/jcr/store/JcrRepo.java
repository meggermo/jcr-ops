package nl.meg.jcr.store;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public interface JcrRepo<T> extends JcrTyped {

    T load(Node node) throws RepositoryException;

    T save(Node node, T entity) throws RepositoryException;
}
