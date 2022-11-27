package nl.meg.jcr.store;

import java.util.List;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.VersionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.meg.jcr.function.JcrFunction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityRepositoryFactoryTest {

    @Test
    void reading(
            @Mock JcrFunction<Credentials, Session> repoLogin,
            @Mock JcrRepo<Long> jcrRepo,
            @Mock Session session,
            @Mock Credentials credentials,
            @Mock Node node
    ) throws RepositoryException {

        when(repoLogin.apply(any())).thenReturn(session);
        when(session.getNode(anyString())).thenReturn(node);

        final var factory = new EntityRepositoryFactory(repoLogin);
        final var repository = factory.make(jcrRepo, List.of("a"));
        assertThat(repository.read(credentials).isRight()).isTrue();
    }

    @Test
    void writing(
            @Mock JcrFunction<Credentials, Session> repoLogin,
            @Mock JcrRepo<Long> jcrRepo,
            @Mock Session session,
            @Mock Credentials credentials,
            @Mock Node node
    ) throws RepositoryException {

        when(repoLogin.apply(any())).thenReturn(session);
        when(session.getNode(anyString())).thenReturn(node);
        when(session.hasPendingChanges()).thenReturn(true);

        final var factory = new EntityRepositoryFactory(repoLogin);
        final var repository = factory.make(jcrRepo, List.of("a"));
        assertThat(repository.write(credentials, 0L).isRight()).isTrue();
    }

    @Test
    void writing_throws(
            @Mock JcrFunction<Credentials, Session> repoLogin,
            @Mock JcrRepo<Long> jcrRepo,
            @Mock Session session,
            @Mock Credentials credentials,
            @Mock Node node
    ) throws RepositoryException {

        when(repoLogin.apply(any())).thenReturn(session);
        when(session.getNode(anyString())).thenReturn(node);
        when(session.hasPendingChanges()).thenReturn(true);
        doThrow(new VersionException()).when(session).save();

        final var factory = new EntityRepositoryFactory(repoLogin);
        final var repository = factory.make(jcrRepo, List.of("a"));
        final var exception = repository.write(credentials, 0L).fromLeft();
        assertThat(exception).isInstanceOf(VersionException.class);
    }

}