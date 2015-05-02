package nl.meg.cr.internal;

import nl.meg.cr.LoginException;
import nl.meg.cr.Repository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.jcr.Credentials;
import javax.jcr.RepositoryException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class RepositoryImplTest extends AbstractMockitoTest {

    @Mock
    private javax.jcr.Repository r;

    @Mock
    private javax.jcr.Session s;

    private Repository repository;

    @Before
    public void setUp() {
        this.repository = new RepositoryImpl(r);
    }

    @Test(expected = LoginException.class)
    public void testLoginFailsDueToLoginException() throws LoginException, RepositoryException {
        when(r.login(Mockito.any(Credentials.class))).thenThrow(javax.jcr.LoginException.class);
        repository.login("username", "password");
    }

    @Test(expected = nl.meg.cr.RepositoryException.class)
    public void testLoginFailsDueUnexpectedException() throws LoginException, RepositoryException {
        when(r.login(Mockito.any(Credentials.class))).thenThrow(RepositoryException.class);
        repository.login("username", "password");
    }

    @Test
    public void testLoginPasses() throws LoginException, RepositoryException {
        when(r.login(Mockito.any(Credentials.class))).thenReturn(s);
        assertThat(repository.login("username", "password"), is(new SessionImpl(s)));
    }

    @Test
    public void testEqualsAndHashCode() {
        assertThat(repository.equals(null), is(false));
        assertThat(repository.equals("test"), is(false));
        assertThat(repository.equals(repository), is(true));
        assertThat(repository.equals(new RepositoryImpl(r)), is(true));
        assertThat(repository.hashCode(), is(new RepositoryImpl(r).hashCode()));
    }
}
