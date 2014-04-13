package nl.meg.jcr;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HippoItemTest {

    private HippoItem<Item> hippoItem;

    @Mock
    private HippoNode hippoNode;

    @Mock
    private Item item;

    @Mock
    private Session session;

    @Mock
    private Node node;

    @Before
    public void setUp() {
        this.hippoItem = new HippoItem<Item>() {
            @Override
            public HippoNode apply(Node node) {
                return hippoNode;
            }

            @Override
            public Item get() {
                return item;
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
    public void testGetParent() throws RepositoryException {
        when(item.getParent()).thenReturn(node);
        when(hippoNode.get()).thenReturn(node);
        assertThat(hippoItem.getParent().get().get(), is(node));
    }

    @Test
    public void testGetParent_IsAbsent() throws RepositoryException {
        when(item.getParent()).thenThrow(ItemNotFoundException.class);
        assertThat(hippoItem.getParent().isPresent(), is(false));
    }
}
