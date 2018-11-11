package nl.meg.jcr.function;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public interface JcrProperty<V> {

    String getName();

    V getValue(Node node) throws RepositoryException;

    JcrProperty<V> setValue(final Node node, final V value) throws RepositoryException;
}
