package nl.meg.jcr.store.internal;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.store.JcrNodeStore;
import nl.meg.jcr.store.JcrProperty;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.doReturn;
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

    @BeforeEach
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

        doReturn(propertyMock).when(nodeMock).getProperty("enabled");
        when(propertyMock.getBoolean()).thenReturn(true);

        doReturn(propertyMock).when(nodeMock).getProperty("modifiedAt");
        when(propertyMock.getDate()).thenReturn(Calendar.getInstance());

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

        private static final JcrProperty<Instant> MODIFIED_AT = JcrPropertyFactory.ofInstant("modifiedAt");
        private Instant modifiedAt;

        private static final JcrProperty<Optional<List<String>>> URLS = JcrPropertyFactory.ofStringListOption("urls");
        private List<String> urls;

        public EnabledNode(final Node node) throws RepositoryException {
            super(node);
            this.enabled = ENABLED.getValue(node);
            this.modifiedAt = MODIFIED_AT.getValue(node);
            this.urls = URLS.getValue(node).orElse(emptyList());
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(final boolean enabled) {
            this.enabled = enabled;
        }

        public Instant getModifiedAt() {
            return modifiedAt;
        }

        public void setModifiedAt(final Instant modifiedAt) {
            this.modifiedAt = modifiedAt;
        }

        public List<String> getUrls() {
            return urls;
        }

        public void setUrls(final List<String> urls) {
            this.urls = urls;
        }

        @Override
        protected void doWriteValues(final Node node) throws RepositoryException {
            ENABLED.setValue(node, enabled);
            MODIFIED_AT.setValue(node, modifiedAt);
            URLS.setValue(node, urls.isEmpty() ? Optional.empty() : Optional.of(urls));
        }
    }

}
