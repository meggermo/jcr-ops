package nl.meg.jcr.mutation.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.mutation.NodeMethods;
import org.junit.Before;
import org.junit.Test;

import javax.jcr.RepositoryException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NodeMethodsImplTest extends AbstractMockitoTest {

    private NodeMethods nodeMethods;

    @Before
    public void setUp() {
        this.nodeMethods = new NodeMethodsImpl();
    }

    @Test
    public void testMove() {
        assertThat(nodeMethods.moveFunction(null) instanceof MoveNodeImpl, is(true));
    }

    @Test
    public void testRename() {
        assertThat(nodeMethods.renameFunction("newName") instanceof  RenameNodeImpl, is(true));
    }

    @Test
    public void testReposition() throws RepositoryException {
        assertThat(nodeMethods.repositionFunction(1) instanceof RepositionNodeImpl, is(true));
    }


}
