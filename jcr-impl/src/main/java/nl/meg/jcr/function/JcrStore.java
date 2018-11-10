package nl.meg.jcr.function;

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

public final class JcrStore {

    private final Repository repository;

    public JcrStore(final Repository repository) {
        this.repository = repository;
    }

    public JcrResult<Session, ?> getSession(final Credentials credentials, final String workspace) {
        return JcrResult
                .startWith(credentials, login(workspace))
                .switchContext((c, s) -> s, c -> {});
    }

    public <R> JcrResult<Session, R> read(JcrResult<Session, ?> session, JcrFunction<Session, R> task) {
        return session.andThen(task);
    }

    public <R> JcrResult<Session, R> write(JcrResult<Session, ?> session, JcrFunction<Session, R> task) {
        return session
                .andThen(task)
                .andCall(this::save);
    }

    public <R> JcrEither<RepositoryException, R> read(Credentials credentials, String workspace, JcrFunction<Session, R> task) {
        return read(getSession(credentials, workspace), task)
                .closeContext(Session::logout)
                .getState();
    }

    public <R> JcrEither<RepositoryException, R> write(Credentials credentials, String workspace, JcrFunction<Session, R> task) {
        return write(getSession(credentials, workspace), task)
                .closeContext(Session::logout)
                .getState();
    }

    public void save(final Session session) throws RepositoryException {
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
        }
    }

    private JcrFunction<Credentials, Session> login(String workspace) {
        return credentials -> repository.login(credentials, workspace);
    }
}
