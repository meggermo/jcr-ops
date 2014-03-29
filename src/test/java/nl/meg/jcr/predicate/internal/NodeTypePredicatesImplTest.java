package nl.meg.jcr.predicate.internal;

import com.google.common.base.Function;
import nl.meg.jcr.function.NodeTypeFunctions;
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

    @Mock
    private NodeTypeFunctions nodeTypeFunctions;
    @Mock
    private Function<NodeType, String> nameF;

    @Before
    public void setUp() {
        this.nodeTypePredicates = new NodeTypePredicatesImpl(nodeTypeFunctions);
    }

    @Test
    public void testNodeTypeIn() {
        when(nodeTypeFunctions.getName()).thenReturn(nameF);
        when(nameF.apply(n)).thenReturn("X");
        assertThat(nodeTypePredicates.nodeTypeIn("X", "Y").apply(n), is(true));
        assertThat(nodeTypePredicates.nodeTypeIn("A", "B").apply(n), is(false));
    }
}
