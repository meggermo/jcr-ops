package nl.meg.cr.internal;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import java.util.stream.Stream;

import static java.util.Spliterator.*;
import static java.util.Spliterators.spliterator;
import static java.util.stream.StreamSupport.stream;

final class RangeIteratorSupport {

    public static final int CHARACTERISTICS = SIZED | CONCURRENT | NONNULL;

    @SuppressWarnings("unchecked")
    static Stream<Node> asStream(NodeIterator iterator) {
        return stream(spliterator(iterator, iterator.getSize(), CHARACTERISTICS), false);
    }

}
