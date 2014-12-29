package nl.meg.jcr.internal;

import nl.meg.AbstractMockitoTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class AbstractHippoItemTest extends AbstractMockitoTest {

    private AbstractHippoItem<Item> hippoItem;

    @Mock
    private Item item;

    @Mock
    private Node node;

    @Mock
    private Property property;
    @Mock
    private Value value;
    @Mock
    private Session session;

    @Before
    public void setUp() {
        this.hippoItem = new AbstractHippoItem<Item>(item, new HippoEntityFactoryImpl()) {
        };
    }

    @Test
    public void testGet() {
        assertThat(hippoItem.get(), is(item));
    }

    @Test
    public void testGetParent() throws RepositoryException {
        when(item.getParent()).thenReturn(node);
        assertThat(hippoItem.getParent().get().get(), is(node));
    }

    @Test
    public void testGetName() throws RepositoryException {
        when(item.getName()).thenReturn("x");
        assertThat(hippoItem.getName(), is("x"));
    }

    @Test
    public void testGetPath() throws RepositoryException {
        when(item.getPath()).thenReturn("x");
        assertThat(hippoItem.getPath(), is("x"));
    }

    @Test
    public void testGetSession() throws RepositoryException {
        when(item.getSession()).thenReturn(session);
        assertThat(hippoItem.getSession(), is(session));
    }

    @Test
    public void testGetDepth() throws RepositoryException {
        when(item.getDepth()).thenReturn(1);
        assertThat(hippoItem.getDepth(), is(1));
    }

    @Test
    public void testIsModified() {
        when(item.isModified()).thenReturn(true);
        assertThat(hippoItem.isModified(), is(true));
    }

    @Test
    public void testIsNew() {
        when(item.isNew()).thenReturn(true);
        assertThat(hippoItem.isNew(), is(true));
    }

    @Test
    public void testIsNode() {
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
        when(item.getAncestor(10)).thenReturn(item);
        assertThat(hippoItem.getAncestor(10), is(item));
    }

}
