package nl.meg.jcr.store.internal;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import nl.meg.jcr.function.JcrEither;
import nl.meg.jcr.function.JcrFunction;
import nl.meg.jcr.store.JcrNode;
import nl.meg.jcr.store.JcrNodeStore;
import nl.meg.jcr.store.JcrStore;

public class JcrNodeStoreImpl implements JcrNodeStore {

    private final JcrStore jcrStore;

    public JcrNodeStoreImpl(final JcrStore jcrStore) {
        this.jcrStore = jcrStore;
    }

    @Override
    public <T extends JcrNode> JcrEither<RepositoryException, T> load(Credentials credentials, String absPath, JcrFunction<Node, T> constructor) {
        return jcrStore.read(credentials,
                session -> constructor.apply(session.getNode(absPath)));
    }

    @Override
    public JcrEither<RepositoryException, JcrNode> save(Credentials credentials, JcrNode jcrNode) {
        return jcrStore.write(credentials,
                session -> jcrNode.writeValues(session.getNode(jcrNode.getAbsPath())));
    }
}
