package nl.meg.cr.internal;

import nl.meg.cr.Node;
import nl.meg.cr.RepositoryException;
import nl.meg.cr.Session;

import java.util.Optional;

final class SessionImpl implements Session {

    private final javax.jcr.Session delegate;

    SessionImpl(javax.jcr.Session session) {
        this.delegate = session;
    }

    @Override
    public Optional<Node> getNode(String absPath) {
        try {
            return Optional.of(new NodeImpl(delegate.getNode(absPath)));
        } catch (javax.jcr.PathNotFoundException e) {
            return Optional.empty();
        } catch (javax.jcr.RepositoryException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionImpl session = (SessionImpl) o;

        return delegate.equals(session.delegate);

    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
}
