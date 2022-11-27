package nl.meg.jcr.store;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public interface JcrProperty<V> {

    String name();

    V getValue(Node node) throws RepositoryException;

    void setValue(final Node node, final V value) throws RepositoryException;
}
