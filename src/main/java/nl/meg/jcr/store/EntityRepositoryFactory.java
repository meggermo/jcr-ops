package nl.meg.jcr.store;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        final var getNode = getNodeFunction(pathSegments, jcrRepo.nodeType());
        final var reader = getReader(jcrRepo, getNode);
        final var writer = getWriter(jcrRepo, getNode);
        return new EntityRepository<>() {
            @Override
            public JcrEither<RepositoryException, E> read(final Credentials credentials) {
                return reader.apply(credentials);
            }

            @Override
            public JcrEither<RepositoryException, E> write(final Credentials credentials, final E entity) {
                return writer.apply(credentials, entity);
            }
        };
    }

    private JcrFunction<Session, Node> getNodeFunction(List<String> pathSegments, String nodeType) {
        final var absPath = pathSegments
                .subList(0, pathSegments.size() - 1)
                .stream()
                .collect(joining("/", "/", ""));
        final var relPath = pathSegments.get(pathSegments.size() - 1);
        return session -> {
            final var parent = session.getNode(absPath);
            if (!parent.hasNode(relPath)) {
                return parent.addNode(relPath, nodeType);
            }
            return parent.getNode(relPath);
        };
    }

    private <E> Function<Credentials, JcrEither<RepositoryException, E>> getReader(JcrRepo<E> jcrRepo, JcrFunction<Session, Node> getNode) {
        final var doLoad = getNode.andThen(jcrRepo::load);
        return credentials -> login(credentials)
                .andThen(doLoad)
                .closeContext(Session::logout)
                .getState();

    }

    private <E> BiFunction<Credentials, E, JcrEither<RepositoryException, E>> getWriter(JcrRepo<E> jcrRepo, JcrFunction<Session, Node> getNode) {
        final Function<E, JcrFunction<Session, E>> doSave = entity -> getNode.andThen(n -> jcrRepo.save(n, entity));
        return (credentials, entity) -> login(credentials)
                .andThen(doSave.apply(entity))
                .andCall(EntityRepositoryFactory.this::saveKeepingChanges)
                .closeContext(Session::logout)
                .getState();
    }

    private JcrMonad<Session, Void> login(Credentials credentials) {
        return JcrMonad
                .startWith(credentials, repoLogin)
                .switchContext((ignore, session) -> session)
                .andThen(session -> null);
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
