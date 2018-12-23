package nl.meg.entity.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import nl.meg.jcr.entity.NodeEntity;
import nl.meg.jcr.function.JcrFunction;

public class NodesReader implements JcrFunction<Node, List<NodeEntity>> {

    private final Predicate<Node> filter;
    private final JcrFunction<Node, NodeEntity> reader;

    public NodesReader(Predicate<Node> filter, JcrFunction<Node, NodeEntity> reader) {
        Objects.requireNonNull(filter);
        Objects.requireNonNull(reader);
        this.filter = filter;
        this.reader = reader;
    }

    @Override
    public List<NodeEntity> apply(final Node node) throws RepositoryException {
        final List<NodeEntity> entities = new ArrayList<>();
        for (NodeIterator iterator = node.getNodes(); iterator.hasNext(); ) {
            final Node n = iterator.nextNode();
            if (filter.test(n)) {
                entities.add(reader.apply(n));
            }
        }
        return entities;
    }
}
