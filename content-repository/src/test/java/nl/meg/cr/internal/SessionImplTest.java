package nl.meg.cr.internal;

import nl.meg.cr.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class SessionImplTest extends AbstractMockitoTest {

    @Mock
    private javax.jcr.Session s;

    @Mock
    private Node n;

    private Session session;

    @Before
    public void setUp() {
        this.session = new SessionImpl(s);
    }

    @Test
    public void testGetNode() throws RepositoryException {
        when(s.getNode("/path")).thenReturn(n);
        assertThat(session.getNode("/path").get(), is(new NodeImpl(n)));
    }

    @Test
    public void testGetNodeWhenPathNotFound() throws RepositoryException {
        when(s.getNode("/path")).thenThrow(PathNotFoundException.class);
        assertThat(session.getNode("/path"), is(Optional.empty()));
    }

    @Test(expected = nl.meg.cr.RepositoryException.class)
    public void testGetNodeUnhandledException() throws RepositoryException {
        when(s.getNode("/path")).thenThrow(RepositoryException.class);
        assertThat(session.getNode("/path"), is(Optional.empty()));
    }

    @Test
    public void testHashCode() {
        assertThat(session.equals(null), is(false));
        assertThat(session.equals("test"), is(false));
        assertThat(session.equals(session), is(true));
        assertThat(session.equals(new SessionImpl(s)), is(true));
        assertThat(session.hashCode(), is(new SessionImpl(s).hashCode()));
    }
}
