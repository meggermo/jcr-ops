package nl.meg.jcr.traversal.internal;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.traversal.WhileIterables;
import static java.util.Arrays.asList;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class WhileIterablesImplTest extends AbstractMockitoTest {

    private WhileIterables whileIterables;

    @Mock
    private List<Integer> empty;

    @Mock
    private List<Integer> nullValues;

    @Mock
    private Iterator<Integer> iteratorWithNulls;

    @Before
    public void setUp() {
        when(empty.iterator()).thenReturn(Collections.<Integer>emptyIterator());
        this.whileIterables = new WhileIterablesImpl();
    }

    @Test
    public void testTakeWhile() {
        final List<Integer> i = asList(1, 2, 3, 4, 5, 4);
        final List<Integer> j = stream(spliteratorUnknownSize(whileIterables.takeWhile(x -> x != 4, i).iterator(), 0), false).collect(toList());
        assertThat(j, is(asList(1, 2, 3)));
    }

    @Test
    public void testDropWhile() {
        final List<Integer> i = asList(1, 2, 3, 4, 5, 4);
        final List<Integer> j = stream(spliteratorUnknownSize(whileIterables.dropWhile(x -> x != 4, i).iterator(), 0), false).collect(toList());
        assertThat(j, is(asList(5, 4)));
    }

    @Test
    public void testTakeWhile_WithEmptyList() {
        assertThat(whileIterables.takeWhile(x -> true, empty).iterator().hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testTakeWhile_ThrowsNoSuchElement() {
        when(nullValues.iterator()).thenReturn(iteratorWithNulls);
        when(iteratorWithNulls.hasNext()).thenReturn(false);
        whileIterables.takeWhile(x -> true, nullValues).iterator().next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testTakeWhile_ThrowsNoSuchElement_AfterSecond() {
        when(nullValues.iterator()).thenReturn(iteratorWithNulls);
        when(iteratorWithNulls.hasNext()).thenReturn(true, false);
        final Iterator<Integer> iterator = whileIterables.takeWhile(x -> true, nullValues).iterator();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testDropWhile_WithEmptyList() {
        assertThat(whileIterables.dropWhile(x -> true, empty).iterator().hasNext(), is(false));
    }
}
