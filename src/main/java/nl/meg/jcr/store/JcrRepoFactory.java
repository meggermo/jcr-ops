package nl.meg.jcr.store;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.VersionException;

public record JcrRepoFactory(JcrProperty<Optional<Long>> versionProperty) {

    public <E extends JcrVersioned> JcrRepo<E> create(JcrVersionedRepo<E> versionedRepo) {
        return new JcrRepo<>() {
            @Override
            public String nodeType() {
                return versionedRepo.nodeType();
            }

            @Override
            public E load(final Node node) throws RepositoryException {
                final var version = versionProperty
                        .getValue(node)
                        .map(AtomicLong::new)
                        .orElseGet(AtomicLong::new);
                return versionedRepo.load(node, version);
            }

            @Override
            public E save(final Node node, final E entity) throws RepositoryException {
                final var actualValue = entity.version();
                final var expectedValue = versionProperty.getValue(node).orElse(0L);
                if (actualValue.compareAndSet(expectedValue, actualValue.get() + 1)) {
                    versionProperty.setValue(node, Optional.of(actualValue.get()));
                    versionedRepo.save(node, entity);
                    return entity;
                } else {
                    throw new VersionException(
                            "Node at path %s was concurrently modified".formatted(node.getPath()));
                }
            }
        };
    }
}
