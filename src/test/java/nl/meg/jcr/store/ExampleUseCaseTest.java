package nl.meg.jcr.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.meg.jcr.function.JcrFunction;
import static nl.meg.jcr.store.JcrPropertyFactory.ofBoolean;
import static nl.meg.jcr.store.JcrPropertyFactory.ofLongOption;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExampleUseCaseTest {

    record Entity(Boolean active, AtomicLong version, String relPath) implements JcrVersioned {
        Entity copy(boolean active) {
            return new Entity(active, this.version, this.relPath);
        }
    }

    record EntityRepo(JcrProperty<Boolean> active) implements JcrVersionedRepo<Entity> {

        @Override
        public Entity load(final Node node, final AtomicLong version) throws RepositoryException {
            return new Entity(active.getValue(node), version, node.getName());
        }

        @Override
        public void save(final Node node, final Entity entity) throws RepositoryException {
            active.setValue(node, entity.active());
        }

        @Override
        public String nodeType() {
            return "nt:my-entity-type";
        }
    }

    record ComposedEntity(Boolean active, List<Entity> entities, AtomicLong version) implements JcrVersioned {
        ComposedEntity copy(boolean active) {
            return new ComposedEntity(active, this.entities, this.version);
        }
    }

    @Test
    void testEntityRepository(
            @Mock JcrFunction<Credentials, Session> repoLogin,
            @Mock Credentials credentials,
            @Mock Session session,
            @Mock Node node,
            @Mock Property property,
            @Mock ValueFactory valueFactory
    ) throws RepositoryException {

        final var repoFactory = new JcrRepoFactory(ofLongOption("version"));
        final var entityRepositoryFactory = new EntityRepositoryFactory(repoLogin);

        final var entityRepo = repoFactory.create(new EntityRepo(ofBoolean("active")));
        final var repository = entityRepositoryFactory.make(entityRepo, List.of("x", "y", "my-entities"));

        when(repoLogin.apply(credentials)).thenReturn(session);
        when(session.getNode("/x/y")).thenReturn(node);
        when(node.hasNode("my-entities")).thenReturn(false);
        when(node.addNode("my-entities", entityRepo.nodeType())).thenReturn(node);
        when(node.getProperty("active")).thenReturn(property);
        when(property.getBoolean()).thenReturn(true);

        final var entity = repository.read(credentials).fromRight();
        assertThat(entity.active()).isTrue();
        assertThat(entity.version()).hasValue(0L);

        when(node.getSession()).thenReturn(session);
        when(session.getValueFactory()).thenReturn(valueFactory);

        final var written = repository.write(credentials, entity.copy(false)).fromRight();
        assertThat(written.active()).isFalse();
        assertThat(entity.version()).hasValue(1L);

        verify(node).setProperty("active", false);
    }

    record ComposedEntityRepo(JcrProperty<Boolean> active,
                              JcrRepo<Entity> entityJcrRepo) implements JcrVersionedRepo<ComposedEntity> {

        @Override
        public ComposedEntity load(final Node node, final AtomicLong version) throws RepositoryException {
            final List<Entity> entities = new ArrayList<>();
            for (NodeIterator it = node.getNodes(); it.hasNext(); ) {
                entities.add(entityJcrRepo.load(it.nextNode()));
            }

            return new ComposedEntity(active.getValue(node), entities, version);
        }

        @Override
        public void save(final Node node, final ComposedEntity entity) throws RepositoryException {
            active.setValue(node, entity.active());
            for (final Entity syncRoot : entity.entities()) {
                if (!node.hasNode(syncRoot.relPath())) {
                    node.addNode(syncRoot.relPath(), entityJcrRepo.nodeType());
                }
                entityJcrRepo.save(node.getNode(syncRoot.relPath()), syncRoot);
            }
        }

        @Override
        public String nodeType() {
            return "nt:my-entity-type";
        }
    }

    @Test
    void testComposedEntityRepository(
            @Mock JcrFunction<Credentials, Session> repoLogin,
            @Mock Credentials credentials,
            @Mock Session session,
            @Mock Node node,
            @Mock Property property,
            @Mock ValueFactory valueFactory,
            @Mock NodeIterator nodeIterator
    ) throws RepositoryException {

        final JcrProperty<Optional<Long>> version = ofLongOption("version");
        final var repoFactory = new JcrRepoFactory(version);
        final var entityRepositoryFactory = new EntityRepositoryFactory(repoLogin);

        final JcrProperty<Boolean> active = ofBoolean("active");
        final var entityRepo = repoFactory.create(new EntityRepo(active));
        final var composedEntityRepo = repoFactory.create(new ComposedEntityRepo(active, entityRepo));
        final var repository = entityRepositoryFactory.make(composedEntityRepo, List.of("x", "y", "my-entities"));

        when(repoLogin.apply(credentials)).thenReturn(session);
        when(session.getNode("/x/y")).thenReturn(node);
        when(node.hasNode("my-entities")).thenReturn(false);
        when(node.addNode("my-entities", entityRepo.nodeType())).thenReturn(node);
        when(node.getProperty("active")).thenReturn(property);
        when(property.getBoolean()).thenReturn(true);
        when(node.getNodes()).thenReturn(nodeIterator);
        when(nodeIterator.hasNext()).thenReturn(true, false);
        when(nodeIterator.nextNode()).thenReturn(node);

        final var composedEntity = repository.read(credentials).fromRight();
        assertThat(composedEntity.active()).isTrue();
        assertThat(composedEntity.version()).hasValue(0L);
        assertThat(composedEntity.entities()).hasSize(1);

        when(node.getSession()).thenReturn(session);
        when(session.getValueFactory()).thenReturn(valueFactory);
        when(node.getNode(null)).thenReturn(node);

        final var written = repository.write(credentials, composedEntity.copy(false)).fromRight();
        assertThat(written.active()).isFalse();
        assertThat(composedEntity.version()).hasValue(1L);

    }


}
