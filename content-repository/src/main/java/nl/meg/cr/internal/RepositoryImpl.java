package nl.meg.cr.internal;

import nl.meg.cr.LoginException;
import nl.meg.cr.Repository;
import nl.meg.cr.RepositoryException;
import nl.meg.cr.Session;

import javax.jcr.Credentials;
import javax.jcr.SimpleCredentials;

final class RepositoryImpl implements Repository {

    private final javax.jcr.Repository delegate;

    RepositoryImpl(javax.jcr.Repository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Session login(String username, String password) throws LoginException {
        try {
            return new SessionImpl(delegate.login(createCredentials(username, password.toCharArray())));
        } catch (javax.jcr.LoginException e) {
            throw new LoginException("Failed to login " + username, e);
        } catch (javax.jcr.RepositoryException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepositoryImpl that = (RepositoryImpl) o;

        return delegate.equals(that.delegate);

    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    private Credentials createCredentials(String username, char[] password) {
        return new SimpleCredentials(username, password);
    }
}
