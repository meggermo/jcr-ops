package nl.meg.jcr.function.internal;

import nl.meg.jcr.function.NodeTypeFunctions;
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
public class NodeTypeFunctionsImplTest {

    private NodeTypeFunctions nodeTypeFunctions = new NodeTypeFunctionsImpl();

    @Mock
    private NodeType nodeType;

    @Before
    public void setUp() {

    }

    @Test
    public void testGetName() {
        when(nodeType.getName()).thenReturn("name");
        assertThat(nodeTypeFunctions.getName().apply(nodeType), is("name"));
    }

}
