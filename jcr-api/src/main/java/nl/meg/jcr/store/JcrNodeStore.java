package nl.meg.jcr.store;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import nl.meg.jcr.function.JcrEither;
import nl.meg.jcr.function.JcrFunction;

public interface JcrNodeStore {

    <T extends JcrNode> JcrEither<RepositoryException, T> load(Credentials credentials, String absPath, JcrFunction<Node,T> constructor);

    JcrEither<RepositoryException, JcrNode> save(Credentials credentials, JcrNode jcrNode);
}
