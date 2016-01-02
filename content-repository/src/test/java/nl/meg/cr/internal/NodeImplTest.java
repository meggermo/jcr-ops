package nl.meg.cr.internal;

import nl.meg.cr.Node;
import nl.meg.cr.support.JcrSupport;
import nl.meg.cr.support.NodeSupport;
import nl.meg.cr.support.ValueSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.RepositoryException;
import java.util.Optional;

import static java.util.Arrays.asList;
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

    @Mock
    private javax.jcr.Value v;

    private Node node;
    private NodeSupport nodeSupport;
    private ValueSupport valueSupport;

    @Before
    public void setUp() {
        final JcrSupport jcrSupport = new JcrSupport();
        this.nodeSupport = new NodeSupport(jcrSupport);
        this.valueSupport = new ValueSupport(jcrSupport);
        this.node = new NodeImpl(n, nodeSupport, valueSupport);
    }

    @Test
    public void testGetNodes() throws RepositoryException {
        when(n.getNodes()).thenReturn(getNodeIterator(n));
        assertThat(node.getNodes().collect(toList()), hasItem(new NodeImpl(n, nodeSupport, valueSupport)));
    }

    @Test
    public void testEqualsAndHashCode() {
        assertThat(node.equals(null), is(false));
        assertThat(node.equals("test"), is(false));
        assertThat(node.equals(node), is(true));
        assertThat(node.equals(new NodeImpl(n, nodeSupport, valueSupport)), is(true));
        assertThat(node.hashCode(), is(new NodeImpl(n, nodeSupport, valueSupport).hashCode()));
    }

    @Test
    public void testGetNonExistentStringValue() {
        assertThat(node.getValue("p1", String.class), is(Optional.empty()));
    }

    @Test
    public void testGetExistingStringValue() throws RepositoryException {
        when(n.getProperty("p1")).thenReturn(p);
        when(p.getValue()).thenReturn(v);
        when(v.getString()).thenReturn("value");
        assertThat(node.getValue("p1", String.class).get(), is("value"));
    }

    @Test
    public void testGetNonExistentStringValues() {
        assertThat(node.getValues("p1", String.class), is(Optional.empty()));
    }

    @Test
    public void testGetExistingStringValues() throws RepositoryException {
        when(n.getProperty("p1")).thenReturn(p);
        when(p.getValues()).thenReturn(new javax.jcr.Value[]{v, v});
        when(v.getString()).thenReturn("v1", "v2");
        assertThat(node.getValues("p1", String.class).get(), is(asList("v1", "v2")));
    }
}
