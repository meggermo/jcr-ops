package nl.meg.jcr.store;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.VersionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JcrRepoFactoryTest {

    private record E(AtomicLong version) implements JcrVersioned {
    }

    @Test
    void testSaving(
            @Mock JcrProperty<Optional<Long>> versionProperty,
            @Mock Node node,
            @Mock JcrVersionedRepo<E> jcrVersionedRepo
    ) throws RepositoryException {

        when(versionProperty.getValue(node)).thenReturn(Optional.of(0L));

        final var factory = new JcrRepoFactory(versionProperty);
        final var repo = factory.create(jcrVersionedRepo);
        assertThat(repo.save(node, new E(new AtomicLong(0))).version())
                .hasValue(1L);
    }

    @Test
    void testSavingThrowsVersionException(
            @Mock JcrProperty<Optional<Long>> versionProperty,
            @Mock Node node,
            @Mock JcrVersionedRepo<E> jcrVersionedRepo
    ) throws RepositoryException {

        when(versionProperty.getValue(node)).thenReturn(Optional.of(0L));

        final var factory = new JcrRepoFactory(versionProperty);
        final var repo = factory.create(jcrVersionedRepo);
        assertThatThrownBy(() -> repo.save(node, new E(new AtomicLong(1))))
                .isInstanceOf(VersionException.class);
    }

    @Test
    void testLoading(
            @Mock JcrProperty<Optional<Long>> versionProperty,
            @Mock Node node,
            @Mock JcrVersionedRepo<E> jcrVersionedRepo
    ) throws RepositoryException {

        when(versionProperty.getValue(node)).thenReturn(Optional.of(0L));
        when(jcrVersionedRepo.load(eq(node), any())).thenReturn(new E(new AtomicLong(1)));

        final var factory = new JcrRepoFactory(versionProperty);
        final var repo = factory.create(jcrVersionedRepo);
        assertThat(repo.load(node).version()).hasValue(1L);
    }

    @Test
    void testNodeTypeDelegatedToJcrVersionedRepo(
            @Mock JcrProperty<Optional<Long>> versionProperty,
            @Mock Node node,
            @Mock JcrVersionedRepo<E> jcrVersionedRepo
    ) {

        final var nodeType = "node type";
        when(jcrVersionedRepo.nodeType()).thenReturn(nodeType);

        final var factory = new JcrRepoFactory(versionProperty);
        final var repo = factory.create(jcrVersionedRepo);
        assertThat(repo.nodeType()).isEqualTo(nodeType);
        verify(jcrVersionedRepo).nodeType();
    }

}