package nl.meg.jcr.store;

import java.util.Collection;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public interface JcrNode {

    String getAbsPath();

    Collection<JcrNode> getNodes();

    JcrNode writeValues(Node node) throws RepositoryException;
}
