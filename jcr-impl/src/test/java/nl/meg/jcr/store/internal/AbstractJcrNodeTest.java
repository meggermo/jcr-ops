package nl.meg.jcr.store.internal;

import java.util.ConcurrentModificationException;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mock;

import nl.meg.AbstractMockitoTest;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbstractJcrNodeTest extends AbstractMockitoTest {

    @Mock
    private Node nodeMock;
    @Mock
    private Property propertyMock;
    @Mock
    private Session sessionMock;
    @Mock
    private ValueFactory valueFactoryMock;
    @Mock
    private Value valueMock;

    private static final class ANode extends AbstractJcrNode {

        public ANode(final Node node) throws RepositoryException {
            super(node);
        }

        @Override
        protected void doWriteValues(final Node node) throws RepositoryException {

        }

    }

    @Test
    public void testWriteValuesVersionIncremented() throws RepositoryException {

        when(nodeMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getValueFactory()).thenReturn(valueFactoryMock);

        when(nodeMock.hasProperty("version")).thenReturn(true);
        when(nodeMock.getProperty("version")).thenReturn(propertyMock);
        when(propertyMock.getLong()).thenReturn(1L);

        final ANode aNode = new ANode(nodeMock);

        when(valueFactoryMock.createValue(2L)).thenReturn(valueMock);
        aNode.writeValues(nodeMock);

        verify(nodeMock).setProperty("version", valueMock);
    }

    @Test
    public void testWriteValuesConcurrentModification() throws RepositoryException {

        when(nodeMock.hasProperty("version")).thenReturn(true);
        when(nodeMock.getProperty("version")).thenReturn(propertyMock);
        when(propertyMock.getLong()).thenReturn(1L, 2L);

        final ANode aNode = new ANode(nodeMock);

        Assertions.assertThatThrownBy(() ->
                aNode.writeValues(nodeMock)
        ).isInstanceOf(ConcurrentModificationException.class);
    }

}
