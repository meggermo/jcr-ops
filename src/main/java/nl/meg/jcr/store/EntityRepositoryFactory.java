package nl.meg.jcr.store;

import java.util.List;

import javax.jcr.AccessDeniedException;
import javax.jcr.Credentials;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import nl.meg.jcr.function.JcrEither;
import nl.meg.jcr.function.JcrFunction;
import nl.meg.jcr.function.JcrMonad;
import static java.util.stream.Collectors.joining;

public record EntityRepositoryFactory(JcrFunction<Credentials, Session> repoLogin) {

    public <E> EntityRepository<E> make(JcrRepo<E> jcrRepo, List<String> pathSegments) {

        final JcrFunction<Session, Node> getNode = session -> getNode(session, pathSegments, jcrRepo.nodeType());

        return new EntityRepository<>() {

            @Override
            public JcrEither<RepositoryException, E> read(final Credentials credentials) {
                return login(credentials)
                        .andThen(getNode.andThen(jcrRepo::load))
                        .closeContext(Session::logout)
                        .getState();
            }

            @Override
            public JcrEither<RepositoryException, E> write(final Credentials credentials, final E entity) {
                return login(credentials)
                        .andThen(getNode.andThen(n -> jcrRepo.save(n, entity)))
                        .andCall(EntityRepositoryFactory.this::saveKeepingChanges)
                        .closeContext(Session::logout)
                        .getState();
            }
        };
    }

    private JcrMonad<Session, Void> login(Credentials credentials) {
        return JcrMonad
                .startWith(credentials, repoLogin)
                .switchContext((ignore, session) -> session)
                .andThen(session -> null);
    }

    private Node getNode(Session session, List<String> pathSegments, String nodeType) throws RepositoryException {
        final var absPath = pathSegments
                .subList(0, pathSegments.size() - 1)
                .stream()
                .collect(joining("/", "/", ""));
        final var parent = session.getNode(absPath);
        final var relPath = pathSegments.get(pathSegments.size() - 1);
        if (!parent.hasNode(relPath)) {
            return parent.addNode(relPath, nodeType);
        }
        return parent.getNode(relPath);
    }

    private void saveKeepingChanges(Session session) throws RepositoryException {
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

}
