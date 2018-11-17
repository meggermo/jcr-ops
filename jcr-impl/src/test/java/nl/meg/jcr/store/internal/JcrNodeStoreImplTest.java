package nl.meg.jcr.store.internal;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.store.JcrNodeStore;
import nl.meg.jcr.store.JcrProperty;
import nl.meg.jcr.store.internal.AbstractJcrNode;
import nl.meg.jcr.store.internal.JcrNodeStoreImpl;
import nl.meg.jcr.store.internal.JcrPropertyFactory;
import nl.meg.jcr.store.internal.JcrStoreImpl;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class JcrNodeStoreImplTest extends AbstractMockitoTest {

    private JcrNodeStore store;
    private final Credentials credentials = new SimpleCredentials("userId", "password".toCharArray());

    @Mock
    private Repository repositoryMock;
    @Mock
    private Session sessionMock;
    @Mock
    private Node nodeMock;
    @Mock
    private Property propertyMock;
    @Mock
    private ValueFactory valeuFactoryMock;
    @Mock
    private Value valueMock;

    @Before
    public void setUp() throws RepositoryException {
        store = new JcrNodeStoreImpl(new JcrStoreImpl(repositoryMock, "ws"));
        when(repositoryMock.login(credentials, "ws")).thenReturn(sessionMock);
        when(sessionMock.getNode(Mockito.anyString())).thenReturn(nodeMock);
        when(nodeMock.getPath()).thenReturn("/path/of/node");
        when(nodeMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getValueFactory()).thenReturn(valeuFactoryMock);
    }

    @Test
    public void testLoadAndWrite() throws RepositoryException {

        when(nodeMock.getProperty("enabled")).thenReturn(propertyMock);
        when(propertyMock.getBoolean()).thenReturn(true);

        when(nodeMock.hasProperty("version")).thenReturn(false);
        when(valeuFactoryMock.createValue(1L)).thenReturn(valueMock);

        store.load(credentials, "/x/y/z", EnabledNode::new).eitherAccept(
                e -> Assertions.fail("Did not expect this: %s", e),
                enabledNode -> {
                    Assertions.assertThat(enabledNode.isEnabled()).describedAs("enabled").isTrue();
                    enabledNode.setEnabled(false);
                    store.save(credentials, enabledNode);
                }
        );
        verify(nodeMock).setProperty("version", valueMock);
        verify(nodeMock).setProperty("enabled", false);
    }


    private static class EnabledNode extends AbstractJcrNode {

        private static final JcrProperty<Boolean> ENABLED = JcrPropertyFactory.ofBoolean("enabled");
        private boolean enabled;

        public EnabledNode(final Node node) throws RepositoryException {
            super(node);
            this.enabled = ENABLED.getValue(node);
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(final boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        protected void doWriteValues(final Node node) throws RepositoryException {
            super.doWriteValues(node);
            ENABLED.setValue(node, enabled);
        }
    }

}
