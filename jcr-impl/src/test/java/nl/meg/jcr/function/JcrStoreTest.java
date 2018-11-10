package nl.meg.jcr.function;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JcrStoreTest {

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
    public void initializeStore() {
        jcrStore = new JcrStore(repositoryMock);
    }


    @Test
    public void testJcrStore() throws RepositoryException {
        final JcrFunction<Session, Property> task =
                session -> {
                    final Node node = session.getNodeByIdentifier("id");
                    return node.addNode("x").setProperty("p", "test");
                };

        final Credentials credentials = new SimpleCredentials("", "".toCharArray());

        when(repositoryMock.login(credentials, "")).thenReturn(sessionMock);
        when(sessionMock.getNodeByIdentifier("id")).thenReturn(nodeMock);
        when(nodeMock.addNode("x")).thenReturn(nodeMock);
        when(nodeMock.setProperty("p","test")).thenReturn(propertyMock);

        final JcrEither<?, Property> state = jcrStore.getSession(credentials, "")
                .andThen(task)
                .andCall(Session::save)
                .andCall(Session::logout)
                .getState();

        state.eitherAccept(
                x -> fail("did not expect this: " + x),
                p -> assertThat(p, CoreMatchers.is(propertyMock))
        );
    }

}
