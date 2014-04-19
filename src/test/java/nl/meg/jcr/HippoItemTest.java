package nl.meg.jcr;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Item;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HippoItemTest extends AbstractMockitoTest {

    private HippoItem<Item> hippoItem;

    @Mock
    private HippoNode hippoNode;

    @Mock
    private Item item;

    @Before
    public void setUp() {
        this.hippoItem = new HippoItem<Item>() {
            @Override
            public Item get() {
                return item;
            }
            @Override
            public Optional<HippoNode> getParent() {
                return null;
            }
        };
    }

    @Test
    public void testGetName() throws RepositoryException {
        when(item.getName()).thenReturn("name");
        assertThat(hippoItem.getName(), is("name"));
    }

    @Test
    public void testGetPath() throws RepositoryException {
        when(item.getPath()).thenReturn("path");
        assertThat(hippoItem.getPath(), is("path"));
    }

    @Test
    public void testGetSession() throws RepositoryException {
        final Session session = mock(Session.class);
        when(item.getSession()).thenReturn(session);
        assertThat(hippoItem.getSession(), is(session));
    }

    @Test
    public void testGetDepth() throws RepositoryException {
        when(item.getDepth()).thenReturn(1);
        assertThat(hippoItem.getDepth(), is(1));
    }

    @Test
    public void testIsModified() throws RepositoryException {
        when(item.isModified()).thenReturn(true);
        assertThat(hippoItem.isModified(), is(true));
    }

    @Test
    public void testIsNew() throws RepositoryException {
        when(item.isNew()).thenReturn(true);
        assertThat(hippoItem.isNew(), is(true));
    }

    @Test
    public void testIsNode() throws RepositoryException {
        when(item.isNode()).thenReturn(true);
        assertThat(hippoItem.isNode(), is(true));
    }

    @Test
    public void testIsSame() throws RepositoryException {
        when(item.isSame(item)).thenReturn(true);
        assertThat(hippoItem.isSame(hippoItem), is(true));
    }

    @Test
    public void testGetAncestor() throws RepositoryException {
        when(item.getAncestor(1)).thenReturn(item);
        assertThat(hippoItem.getAncestor(1), is(item));
    }

    @Test
    public void testExceptionTranslation() throws RepositoryException {
        final RepositoryException rre = new RepositoryException();
        final Throwable t = rre;
        try {
            when(item.getSession()).thenThrow(rre);
            hippoItem.getSession();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(item.getName()).thenThrow(rre);
            hippoItem.getName();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
        try {
            when(item.getPath()).thenThrow(rre);
            hippoItem.getPath();
            shouldHaveThrown();
        } catch (RuntimeRepositoryException e) {
            assertThat(e.getCause(), is(t));
        }
    }

    private void shouldHaveThrown() {
        shouldHaveThrown(RuntimeRepositoryException.class);
    }
}
