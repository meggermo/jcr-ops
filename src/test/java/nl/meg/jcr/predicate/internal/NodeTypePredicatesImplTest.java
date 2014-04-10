package nl.meg.jcr.predicate.internal;

import nl.meg.jcr.predicate.NodeTypePredicates;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.nodetype.NodeType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeTypePredicatesImplTest {

    private NodeTypePredicates nodeTypePredicates;

    @Mock
    private NodeType n;

    @Before
    public void setUp() {
        this.nodeTypePredicates = new NodeTypePredicatesImpl();
    }

    @Test
    public void testNodeTypeIn() {
        when(n.getPrimaryItemName()).thenReturn("X");
        assertThat(nodeTypePredicates.nodeTypeIn("X", "Y").test(n), is(true));
        assertThat(nodeTypePredicates.nodeTypeIn("A", "B").test(n), is(false));
    }
}
