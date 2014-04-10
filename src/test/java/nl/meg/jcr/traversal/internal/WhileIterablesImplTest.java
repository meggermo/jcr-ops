package nl.meg.jcr.traversal.internal;

import nl.meg.jcr.traversal.WhileIterables;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.google.common.collect.ImmutableList.copyOf;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WhileIterablesImplTest {

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
        final List<Integer> j = copyOf(whileIterables.takeWhile(x -> x != 4, i));
        assertThat(j, is(asList(1, 2, 3)));
    }

    @Test
    public void testDropWhile() {
        final List<Integer> i = asList(1, 2, 3, 4, 5, 4);
        final List<Integer> j = copyOf(whileIterables.dropWhile(x -> x != 4, i));
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
        when(iteratorWithNulls.next()).thenReturn(1);
        final Iterator<Integer> iterator = whileIterables.takeWhile(x -> true, nullValues).iterator();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testDropWhile_WithEmptyList() {
        assertThat(whileIterables.dropWhile(x -> true, empty).iterator().hasNext(), is(false));
    }
}
