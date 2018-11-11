package nl.meg.jcr.function.internal;

import javax.jcr.AccessDeniedException;
import javax.jcr.Credentials;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import nl.meg.jcr.function.JcrEither;
import nl.meg.jcr.function.JcrFunction;
import nl.meg.jcr.function.JcrResult;
import nl.meg.jcr.function.JcrStore;

public final class JcrStoreImpl implements JcrStore {

    private final Repository repository;

    public JcrStoreImpl(final Repository repository) {
        this.repository = repository;
    }

    @Override
    public <R> JcrEither<RepositoryException, R> read(final Credentials credentials, final String workspace, final JcrFunction<Session, R> task) {
        return login(credentials, workspace)
                .andThen(task)
                .closeContext(Session::logout)
                .getState();
    }

    @Override
    public <R> JcrEither<RepositoryException, R> write(final Credentials credentials, final String workspace, final JcrFunction<Session, R> task) {
        return login(credentials, workspace)
                .andThen(task)
                .andCall(this::save)
                .closeContext(Session::logout)
                .getState();
    }

    private JcrResult<Session, ?> login(final Credentials credentials, final String workspace) {
        return JcrResult
                .startWith(credentials, login(workspace))
                .switchContext((c, s) -> s, c -> {
                });
    }

    private void save(final Session session) throws RepositoryException {
        try {
            if (session.hasPendingChanges()) {
                session.refresh(true);
                session.save();
            }
        } catch (AccessDeniedException
                | ItemExistsException
                | ReferentialIntegrityException
                | ConstraintViolationException
                | InvalidItemStateException
                | VersionException
                | LockException
                | NoSuchNodeTypeException e) {
            session.refresh(false);
            throw e;
        }
    }

    private JcrFunction<Credentials, Session> login(String workspace) {
        return credentials -> repository.login(credentials, workspace);
    }
}
