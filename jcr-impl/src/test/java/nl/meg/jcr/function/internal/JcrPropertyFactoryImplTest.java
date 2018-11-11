package nl.meg.jcr.function.internal;

import java.util.Optional;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.junit.Test;
import org.mockito.Mock;

import nl.meg.AbstractMockitoTest;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JcrPropertyFactoryImplTest extends AbstractMockitoTest {

    @Mock
    private Node nodeMock;
    @Mock
    private Property propertyMock;
    @Mock
    private Value valueMock;
    @Mock
    private Session sessionMock;
    @Mock
    private ValueFactory valueFactoryMock;

    @Test
    public void testGetAndSet() throws RepositoryException {

        when(nodeMock.getProperty("state")).thenReturn(propertyMock);
        when(propertyMock.getString()).thenReturn(ReplicationState.FULL_SYNC.name());

        when(nodeMock.hasProperty("lastSyncLogId")).thenReturn(true);
        when(nodeMock.getProperty("lastSyncLogId")).thenReturn(propertyMock);
        when(propertyMock.getLong()).thenReturn(1L);

        when(nodeMock.hasProperty("syncRoots")).thenReturn(true);
        when(nodeMock.getProperty("syncRoots")).thenReturn(propertyMock);
        final Value[] vs = {valueMock};
        when(propertyMock.getValues()).thenReturn(vs);
        when(valueMock.getString()).thenReturn("TEST");

        final ControlConfig config = ControlNode.getConfig(nodeMock);
        assertThat(config.getState()).isEqualByComparingTo(ReplicationState.FULL_SYNC);
        assertThat(config.getLastSyncLogId()).isEqualTo(Optional.of(1L));
        assertThat(config.getSyncRootPaths()).isEqualTo(Optional.of(singletonList("TEST")));

        when(nodeMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getValueFactory()).thenReturn(valueFactoryMock);

        config.setState(ReplicationState.DELTA_SYNC);
        ControlNode.update(nodeMock, config);

        verify(nodeMock).setProperty("state", ReplicationState.DELTA_SYNC.name());
    }


}


