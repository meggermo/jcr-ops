package nl.meg.jcr.store.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.meg.jcr.store.EntityRepositoryFactory;
import nl.meg.jcr.store.JcrRepoFactory;
import static javax.jcr.nodetype.NodeType.NT_UNSTRUCTURED;
import static nl.meg.jcr.store.JcrPropertyFactory.ofLongOption;
import static nl.meg.jcr.store.JcrPropertyFactory.ofString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JcrStoreImplTest {

    private final Credentials credentials = new SimpleCredentials("userID", "password".toCharArray());
    private final String workspace = "some-workspace-name";
    private EntityRepositoryFactory entityRepositoryFactory;

    @Mock
    private Repository repositoryMock;
    @Mock
    private Session sessionMock;
    @Mock
    private Node nodeMock;
    @Mock
    private Property propertyMock;

    @BeforeEach
    void setUp() {
        entityRepositoryFactory = new EntityRepositoryFactory(c -> repositoryMock.login(c, workspace));
    }

    @Nested
    class ExampleUseCase {

        @Mock
        private ValueFactory valueFactoryMock;
        @Mock
        private Value valueMock;

        @Mock
        private NodeIterator nodeIterator;

        @Test
        void readControlNode() throws RepositoryException {

            final String version = "version";
            final var jcrRepoFactory = new JcrRepoFactory(ofLongOption(version));

            final String syncRoots = "syncRoots";
            final var syncRootRepo = jcrRepoFactory.create(new SyncRootRepo(NT_UNSTRUCTURED, ofString(syncRoots)));

            final String state = "state";
            final String lastSyncLogId = "lastSyncLogId";
            final var controlConfigRepo = jcrRepoFactory.create(
                    new ControlConfigRepo(NT_UNSTRUCTURED, ofString(state), ofLongOption(lastSyncLogId), syncRootRepo));
            final var segments = List.of("foo", "bar", "baz");
            final var controlConfigEntityRepository = entityRepositoryFactory.make(controlConfigRepo, segments);

            when(repositoryMock.login(credentials, workspace)).thenReturn(sessionMock);
            when(sessionMock.getNode("/foo/bar")).thenReturn(nodeMock);
            when(nodeMock.hasNode("baz")).thenReturn(true);
            when(nodeMock.getNode("baz")).thenReturn(nodeMock);

            doReturn(propertyMock).when(nodeMock).getProperty(state);
            doReturn(ReplicationState.FULL_SYNC.name()).when(propertyMock).getString();

            doReturn(false).when(nodeMock).hasProperty(version);
            doReturn(valueMock).when(valueFactoryMock).createValue(1L);
            doReturn(valueMock).when(valueFactoryMock).createValue(10L);

            doReturn(true).when(nodeMock).hasProperty(lastSyncLogId);
            doReturn(propertyMock).when(nodeMock).getProperty(lastSyncLogId);
            doReturn(10L).when(propertyMock).getLong();

            doReturn(false).when(nodeMock).hasNode("sr1");
            doReturn(nodeMock).when(nodeMock).getNode("sr1");

            doReturn(sessionMock).when(nodeMock).getSession();
            doReturn(valueFactoryMock).when(sessionMock).getValueFactory();
            doReturn(nodeIterator).when(nodeMock).getNodes(anyString());
            doReturn(false).when(nodeIterator).hasNext();

            final var read = controlConfigEntityRepository.read(credentials).fromRight();
            assertThat(read.state()).isEqualByComparingTo(ReplicationState.FULL_SYNC);
            assertThat(read.lastSyncLogId()).isEqualTo(10L);
            assertThat(read.syncRoots()).isEmpty();

            final var modified = read.copy(ReplicationState.STOPPED).copy(List.of(new SyncRoot(new AtomicLong(0L), "sr1", "/a/b/c")));

            final var written = controlConfigEntityRepository.write(credentials, modified).fromRight();
            assertThat(written.state()).isEqualByComparingTo(modified.state());
            assertThat(written.lastSyncLogId()).isEqualTo(modified.lastSyncLogId());
            assertThat(written.version()).hasValue(1L);
            assertThat(read.version()).hasSameHashCodeAs(written.version());

            verify(nodeMock, times(3)).hasProperty(version);
            // 1x reading 2x writing

            verify(nodeMock, times(2)).setProperty(version, valueMock);
            verify(nodeMock).setProperty(lastSyncLogId, valueMock);
            verify(nodeMock).setProperty(syncRoots, "/a/b/c");
        }

    }

}
