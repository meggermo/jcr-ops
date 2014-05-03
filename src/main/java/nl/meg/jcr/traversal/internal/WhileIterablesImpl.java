package nl.meg.jcr.traversal.internal;

import nl.meg.jcr.traversal.WhileIterables;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

final class WhileIterablesImpl implements WhileIterables {

    @Override
    public <X> Iterable<X> takeWhile(final Predicate<X> p, final Iterable<X> iterable) {
        return new Iterable<X>() {
            @Override
            public Iterator<X> iterator() {
                final Iterator<X> iterator = iterable.iterator();
                return iterator.hasNext() ? new TakeWhileIterator<>(iterator, p) : Collections.<X>emptyIterator();
            }
        };
    }

    @Override
    public <X> Iterable<X> dropWhile(final Predicate<X> p, Iterable<X> iterable) {
        final Iterator<X> i = iterable.iterator();
        for (; i.hasNext() && p.test(i.next()); ) ;
        return () -> i;
    }

    private static class TakeWhileIterator<S> implements Iterator<S> {

        private final Iterator<S> iterator;
        private final Predicate<S> p;
        private S value;

        private TakeWhileIterator(Iterator<S> iterator, Predicate<S> p) {
            this.iterator = iterator;
            this.p = p;
            this.value = nextValue(iterator);
        }

        @Override
        public boolean hasNext() {
            return p.test(value);
        }

        @Override
        public S next() {
            if (value != null) {
                final S next = value;
                value = nextValue(iterator);
                return next;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {

        }

        private static <X> X nextValue(Iterator<X> iterator) {
            return iterator.hasNext() ? iterator.next() : null;
        }
    }

}
