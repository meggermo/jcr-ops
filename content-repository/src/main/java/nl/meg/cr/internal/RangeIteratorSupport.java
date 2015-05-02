package nl.meg.cr.internal;

import javax.jcr.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.*;
import static java.util.Spliterators.spliterator;

final class RangeIteratorSupport {

    @SuppressWarnings("unchecked")
    static Stream<Node> stream(NodeIterator iterator) {
        return asStream(iterator);
    }

    @SuppressWarnings("unchecked")
    static Stream<Property> stream(PropertyIterator iterator) {
        return asStream(iterator);
    }

    @SuppressWarnings("unchecked")
    static <T extends RangeIterator> Stream asStream(T iterator) {
        int characteristics = SIZED | CONCURRENT | NONNULL;
        return StreamSupport.stream(spliterator(iterator, iterator.getSize(), characteristics), false);
    }
}
