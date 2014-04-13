package nl.meg.jcr.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Node;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HippoNodeImplTest extends AbstractMockitoTest {

    private HippoNode hippoNode;

    @Mock
    private Node node;

    @Before
    public void setUp() {
        this.hippoNode = new HippoNodeImpl(node);
    }

    @Test
    public void testGet() {
        assertThat(hippoNode.get(), is(node));
    }

    @Test
    public void testApply() {
        assertThat(HippoNodeImpl.class.isInstance(hippoNode.apply(node)), is(true));
    }
}
