package nl.meg.jcr.store.internal;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.version.VersionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.meg.jcr.function.JcrFunction;
import nl.meg.jcr.store.JcrStore;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JcrStoreImplTest {

    private final Credentials credentials = new SimpleCredentials("userID", "password".toCharArray());
    private final String workspace = "some-workspace-name";
    private JcrStore jcrStore;

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
        jcrStore = new JcrStoreImpl(repositoryMock, workspace);
    }

    @Test
    void testRead() throws RepositoryException {
        when(repositoryMock.login(credentials, workspace)).thenReturn(sessionMock);
        jcrStore.read(credentials, session -> 10L).eitherAccept(
                JcrStoreImplTest::unexpected,
                r -> assertThat(r).isEqualTo(10L)
        );
    }


    @Test
    void testWrite() throws RepositoryException {

        final Credentials credentials = new SimpleCredentials("userID", "password".toCharArray());
        final String workspace = "some-workspace-name";
        when(repositoryMock.login(credentials, workspace)).thenReturn(sessionMock);

        when(sessionMock.getNodeByIdentifier("id")).thenReturn(nodeMock);
        when(nodeMock.addNode("x")).thenReturn(nodeMock);
        when(nodeMock.setProperty("p", "test")).thenReturn(propertyMock);
        when(sessionMock.hasPendingChanges()).thenReturn(true);

        final JcrFunction<Session, Property> task =
                session -> {
                    final Node node = session.getNodeByIdentifier("id");
                    return node.addNode("x").setProperty("p", "test");
                };
        jcrStore.write(credentials, task).eitherAccept(
                JcrStoreImplTest::unexpected,
                p -> assertThat(p).isEqualTo(propertyMock)
        );

        verify(sessionMock, times(1)).refresh(true);
        verify(sessionMock, times(1)).save();
    }

    @Test
    void testChangesDiscardedOnSaveException() throws RepositoryException {

        final Credentials credentials = new SimpleCredentials("userID", "password".toCharArray());
        final String workspace = "some-workspace-name";
        when(repositoryMock.login(credentials, workspace)).thenReturn(sessionMock);
        when(sessionMock.hasPendingChanges()).thenReturn(true);
        doThrow(new VersionException()).when(sessionMock).save();

        jcrStore.write(credentials, session -> "TEST").eitherAccept(
                e -> assertThat(e).isInstanceOf(VersionException.class),
                x -> fail("did not expect this: %s", x)
        );

        verify(sessionMock).refresh(false);
    }

    @Test
    void testSaveNotCalledIfTherAreNoChanges() throws RepositoryException {

        final Credentials credentials = new SimpleCredentials("userID", "password".toCharArray());
        final String workspace = "some-workspace-name";
        when(repositoryMock.login(credentials, workspace)).thenReturn(sessionMock);
        when(sessionMock.hasPendingChanges()).thenReturn(false);

        jcrStore.write(credentials, session -> "TEST").eitherAccept(
                JcrStoreImplTest::unexpected,
                s -> assertThat(s).isEqualTo("TEST")
        );

        verify(sessionMock, times(0)).save();
    }

    @Nested
    class ExampleUseCase {
        @Test
        void readControlNode() throws RepositoryException {

            when(repositoryMock.login(credentials, workspace)).thenReturn(sessionMock);

            when(sessionMock.getNode("/foo")).thenReturn(nodeMock);

            doReturn(propertyMock).when(nodeMock).getProperty("state");
            doReturn(ReplicationState.FULL_SYNC.name()).when(propertyMock).getString();

            doReturn(true).when(nodeMock).hasProperty("lastSyncLogId");
            doReturn(propertyMock).when(nodeMock).getProperty("lastSyncLogId");
            doReturn(10L).when(propertyMock).getLong();

            final JcrFunction<Session, Node> getNode = session -> session.getNode("/foo");

            jcrStore.read(credentials, getNode.andThen(ControlNode::getConfig))
                    .eitherAccept(
                            JcrStoreImplTest::unexpected,
                            c -> {
                                assertThat(c.getState())
                                        .isEqualByComparingTo(ReplicationState.FULL_SYNC);
                                assertThat(c.getLastSyncLogId())
                                        .hasValue(10L);
                            }
                    );
        }

    }

    private static void unexpected(Object value) {
        fail("did not expect this: %s", value);
    }
}
