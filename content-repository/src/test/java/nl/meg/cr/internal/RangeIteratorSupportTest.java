package nl.meg.cr.internal;

import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Node;
import javax.jcr.Property;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static nl.meg.cr.internal.RangeIteratorSupport.stream;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class RangeIteratorSupportTest extends AbstractMockitoTest {

    @Mock
    private Node n1, n2;

    @Mock
    private Property p1, p2;

    @Test
    public void testNodeStream() {
        final List<Node> ns = new RangeIteratorSupport().stream(getNodeIterator(n1, n2)).collect(toList());
        assertThat(ns.size(), is(2));
        assertThat(ns, hasItems(n1, n2));
    }

    @Test
    public void testPropertyStream() {
        final List<Property> ps = stream(getPropertyIterator(p1, p2)).collect(toList());
        assertThat(ps.size(), is(2));
        assertThat(ps, hasItems(p1, p2));
    }
}

