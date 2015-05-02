package nl.meg.cr.internal;

import nl.meg.cr.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.RepositoryException;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class NodeImplTest extends AbstractMockitoTest {

    @Mock
    private javax.jcr.Node n;

    @Mock
    private javax.jcr.Property p;

    private Node node;

    @Before
    public void setUp() {
        this.node = new NodeImpl(n);
    }

    @Test
    public void testGetNodes() throws RepositoryException {
        when(n.getNodes()).thenReturn(getNodeIterator(n));
        assertThat(node.getNodes().collect(toList()), hasItem(new NodeImpl(n)));
    }

    @Test
    public void testGetProperties() throws RepositoryException {
        when(n.getProperties()).thenReturn(getPropertyIterator(p));
        assertThat(node.getProperties().collect(toList()), hasItem(new PropertyImpl(p)));
    }

    @Test
    public void testEqualsAndHashCode() {
        assertThat(node.equals(null), is(false));
        assertThat(node.equals("test"), is(false));
        assertThat(node.equals(node), is(true));
        assertThat(node.equals(new NodeImpl(n)), is(true));
        assertThat(node.hashCode(), is(new NodeImpl(n).hashCode()));
    }
}
