package nl.meg.jcr.function.internal;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.version.VersionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import nl.meg.jcr.function.JcrFunction;
import nl.meg.jcr.function.JcrStore;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JcrStoreImplTest {

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

    @Before
    public void setUp() throws RepositoryException {
        jcrStore = new JcrStoreImpl(repositoryMock);
    }

    @Test
    public void testRead() throws RepositoryException {
        when(repositoryMock.login(credentials, workspace)).thenReturn(sessionMock);
        jcrStore.read(credentials, workspace, session -> 10L).eitherAccept(
                e -> fail("did not expect this: " + e),
                r -> assertThat(r, is(10L))
        );
    }


    @Test
    public void testWrite() throws RepositoryException {

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
        jcrStore.write(credentials, workspace, task).eitherAccept(
                e -> fail("did not expect this: " + e),
                p -> assertThat(p, is(propertyMock))
        );

        verify(sessionMock, times(1)).refresh(true);
        verify(sessionMock, times(1)).save();
    }

    @Test
    public void testChangesDiscardedOnSaveException() throws RepositoryException {

        final Credentials credentials = new SimpleCredentials("userID", "password".toCharArray());
        final String workspace = "some-workspace-name";
        when(repositoryMock.login(credentials, workspace)).thenReturn(sessionMock);
        when(sessionMock.hasPendingChanges()).thenReturn(true);
        doThrow(new VersionException()).when(sessionMock).save();

        jcrStore.write(credentials, workspace, session -> "TEST").eitherAccept(
                e -> assertThat(e, instanceOf(VersionException.class)),
                x -> fail("did not expect this: " + x)
        );

        verify(sessionMock).refresh(false);
    }

    @Test
    public void testSaveNotCalledIfTherAreNoChanges() throws RepositoryException {

        final Credentials credentials = new SimpleCredentials("userID", "password".toCharArray());
        final String workspace = "some-workspace-name";
        when(repositoryMock.login(credentials, workspace)).thenReturn(sessionMock);
        when(sessionMock.hasPendingChanges()).thenReturn(false);

        jcrStore.write(credentials, workspace, session -> "TEST").eitherAccept(
                e -> fail("did not expect this: " + e),
                s -> assertThat(s, is("TEST"))
        );

        verify(sessionMock, times(0)).save();
    }

}