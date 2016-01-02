package nl.meg.cr.support;

import nl.meg.cr.internal.AbstractMockitoTest;
import nl.meg.cr.support.JcrSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class JcrSupportTest extends AbstractMockitoTest {

    @Mock
    private Node node;

    private JcrSupport jcrSupport;

    @Before
    public void initializeClassUnderTest() {
        this.jcrSupport = new JcrSupport();
    }

    @Test
    public void testWrap_returns_null_if_item_not_found() throws RepositoryException {
        when(node.getProperty(Mockito.anyString())).thenThrow(ItemNotFoundException.class);
        assertThat(jcrSupport.wrap((Node n) -> n.getProperty("some")).apply(node), is(nullValue()));
    }

    @Test
    public void testWrap_returns_null_if_path_not_found() throws RepositoryException {
        when(node.getProperty(Mockito.anyString())).thenThrow(PathNotFoundException.class);
        assertThat(jcrSupport.wrap((Node n) -> n.getProperty("some")).apply(node), is(nullValue()));
    }

    @Test
    public void testWrapOptional_returns_empty_if_item_not_found() throws Exception {
        when(node.getProperty(Mockito.anyString())).thenThrow(ItemNotFoundException.class);
        assertThat(jcrSupport.wrapOptional((Node n) -> n.getProperty("some")).apply(node), is(Optional.empty()));
    }

    @Test(expected = nl.meg.cr.RepositoryException.class)
    public void testWrapOptional_throws_on_repository_exception() throws Exception {
        when(node.getProperty(Mockito.anyString())).thenThrow(RepositoryException.class);
        jcrSupport.wrapOptional((Node n) -> n.getProperty("some")).apply(node);
    }
}
