package nl.meg.jcr.store;

import java.util.concurrent.atomic.AtomicLong;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public interface JcrVersionedRepo<E extends JcrVersioned> extends JcrTyped {

    E load(Node node, AtomicLong version) throws RepositoryException;

    void save(Node node, E entity) throws RepositoryException;

}
