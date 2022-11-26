package nl.meg.jcr.store;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.VersionException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JcrRepoFactoryTest {

    private record E(AtomicLong version) implements JcrVersioned {
    }

    @Test
    void create(
            @Mock JcrProperty<Optional<Long>> versionProperty,
            @Mock Node node,
            @Mock JcrVersionedRepo<E> jcrVersionedRepo
    ) throws RepositoryException {

        when(versionProperty.getValue(node)).thenReturn(Optional.of(0L));

        final var factory = new JcrRepoFactory(versionProperty);
        final var repo = factory.create(jcrVersionedRepo);
        Assertions.assertThatThrownBy(() -> repo.save(node, new E(new AtomicLong(1))))
                .isInstanceOf(VersionException.class);
    }
}