package nl.meg.jcr.store;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static javax.jcr.nodetype.NodeType.NT_UNSTRUCTURED;
import static nl.meg.jcr.store.JcrNamed.loadEntities;
import static nl.meg.jcr.store.JcrNamed.saveEntities;
import static nl.meg.jcr.store.JcrPropertyFactory.ofBoolean;
import static nl.meg.jcr.store.JcrPropertyFactory.ofLongOption;
import static org.assertj.core.api.Assertions.assertThat;

class ExampleUseCaseTest {

    private Repository repository;
    private Credentials credentials;

    @BeforeEach
    void initialize() {
        repository = new Jcr(new Oak()).createRepository();
        credentials = new SimpleCredentials("admin", "admin".toCharArray());
    }

    record Entity(Boolean active, AtomicLong version, String name) implements JcrNamed, JcrVersioned {
        Entity copy(boolean active) {
            return new Entity(active, this.version, this.name);
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
            return NT_UNSTRUCTURED;
        }
    }

    record ComposedEntity(Boolean active, List<Entity> entities, AtomicLong version) implements JcrVersioned {
        ComposedEntity copy(List<Entity> entities) {
            return new ComposedEntity(this.active, entities, this.version);
        }
    }

    @Test
    void testEntityRepository() throws RepositoryException {

        final JcrProperty<Boolean> active = ofBoolean("active");
        final var node = repository.login(credentials).getRootNode()
                .addNode("x", NT_UNSTRUCTURED)
                .addNode("y", NT_UNSTRUCTURED)
                .addNode("z", NT_UNSTRUCTURED);
        active.setValue(node, true);
        node.getSession().save();

        final var repoFactory = new JcrRepoFactory(ofLongOption("version"));
        final var entityRepositoryFactory = new EntityRepositoryFactory(c -> repository.login(c));

        final var entityRepo = repoFactory.create(new EntityRepo(active));
        final var entityRepository = entityRepositoryFactory.make(entityRepo, List.of("x", "y", "z"));

        final var entity = entityRepository.read(credentials).fromRight();
        assertThat(entity.active()).isTrue();
        assertThat(entity.version()).hasValue(0L);

        final var written = entityRepository.write(credentials, entity.copy(false)).fromRight();
        assertThat(written.active()).isFalse();
        assertThat(entity.version()).hasValue(1L);

        final var read = entityRepository.read(credentials).fromRight();
        assertThat(read.active()).isFalse();
        assertThat(read.version()).hasValue(1L);

    }

    record ComposedEntityRepo(JcrProperty<Boolean> active,
                              JcrRepo<Entity> entityJcrRepo) implements JcrVersionedRepo<ComposedEntity> {

        @Override
        public ComposedEntity load(final Node node, final AtomicLong version) throws RepositoryException {
            return new ComposedEntity(active.getValue(node), loadEntities(node, entityJcrRepo), version);
        }

        @Override
        public void save(final Node node, final ComposedEntity composedEntity) throws RepositoryException {
            active.setValue(node, composedEntity.active());
            saveEntities(node, "*", composedEntity.entities(), entityJcrRepo);
        }

        @Override
        public String nodeType() {
            return NT_UNSTRUCTURED;
        }
    }

    @Test
    void testComposedEntityRepository() throws RepositoryException {

        final JcrProperty<Boolean> active = ofBoolean("active");
        final var node = repository.login(credentials).getRootNode()
                .addNode("x", NT_UNSTRUCTURED)
                .addNode("y", NT_UNSTRUCTURED)
                .addNode("z", NT_UNSTRUCTURED);
        active.setValue(node, true);
        node.getSession().save();

        final JcrProperty<Optional<Long>> version = ofLongOption("version");
        final var repoFactory = new JcrRepoFactory(version);
        final var entityRepositoryFactory = new EntityRepositoryFactory(c -> repository.login(c));

        final var entityRepo = repoFactory.create(new EntityRepo(active));
        final var composedEntityRepo = repoFactory.create(new ComposedEntityRepo(active, entityRepo));
        final var composedEntityRepository = entityRepositoryFactory.make(composedEntityRepo, List.of("x", "y", "z"));

        final var composedEntity = composedEntityRepository.read(credentials).fromRight();
        assertThat(composedEntity.active()).isTrue();
        assertThat(composedEntity.version()).hasValue(0L);
        assertThat(composedEntity.entities()).isEmpty();

        final var e1 = new Entity(true, new AtomicLong(0), "e1");
        final var e2 = new Entity(true, new AtomicLong(0), "e2");
        composedEntityRepository.write(credentials, composedEntity.copy(List.of(e1, e2)));

        final var read = composedEntityRepository.read(credentials).fromRight();
        assertThat(read.entities()).hasSize(2);

    }


}
