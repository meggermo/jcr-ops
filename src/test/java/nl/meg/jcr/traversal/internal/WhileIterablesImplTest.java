package nl.meg.jcr.traversal.internal;

import com.google.common.base.Predicate;
import nl.meg.jcr.traversal.WhileIterables;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Predicates.*;
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

    @Before
    public void setUp() {
        when(empty.iterator()).thenReturn(Collections.<Integer>emptyIterator());
        this.whileIterables = new WhileIterablesImpl();
    }

    @Test
    public void testTakeWhile() {
        final Predicate<Integer> p = not(equalTo(4));
        final List<Integer> i = asList(1, 2, 3, 4, 5, 4);
        final List<Integer> j = copyOf(whileIterables.takeWhile(p, i));
        assertThat(j, is(asList(1,2,3)));
    }

    @Test
    public void testDropWhile() {
        final Predicate<Integer> p = not(equalTo(4));
        final List<Integer> i = asList(1, 2, 3, 4, 5, 4);
        final List<Integer> j = copyOf(whileIterables.dropWhile(p, i));
        assertThat(j, is(asList(5, 4)));
    }

    @Test
    public void testTakeWhile_WithEmptyList() {
        final Predicate<Integer> p = alwaysTrue();
        assertThat(whileIterables.takeWhile(p, empty).iterator().hasNext(), is(false));
    }

    @Test
    public void testDropWhile_WithEmptyList() {
        final Predicate<Integer> p = alwaysTrue();
        assertThat(whileIterables.dropWhile(p, empty).iterator().hasNext(), is(false));
    }
}
