package nl.meg.jcr;


import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.nodetype.NodeType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Stream;

import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.SIZED;
import static java.util.Spliterators.spliterator;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static nl.meg.function.FunctionAdapter.relax;

public interface HippoNode extends HippoItem<Node> {

    default Integer getIndex() {
        return relax(Node::getIndex, get(), RuntimeRepositoryException::new);
    }

    default String getIdentifier() {
        return relax(Node::getIdentifier, get(), RuntimeRepositoryException::new);
    }

    default boolean isRoot() {
        return relax(n -> n.getSession().getRootNode().isSame(get()), get(), RuntimeRepositoryException::new);
    }

    default boolean isNodeType(String nodeTypeName) {
        return relax(n -> n.isNodeType(nodeTypeName), get(), RuntimeRepositoryException::new);
    }

    default NodeType getPrimaryNodeType() {
        return relax(Node::getPrimaryNodeType, get(), RuntimeRepositoryException::new);
    }

    default NodeType[] getMixinNodeTypes() {
        return relax(Node::getMixinNodeTypes, get(), RuntimeRepositoryException::new);
    }

    default Optional<HippoNode> getNode(String name) {
        return relax(n -> n.hasNode(name)
                        ? Optional.of(apply(n.getNode(name)))
                        : Optional.empty(),
                get(),
                RuntimeRepositoryException::new
        );
    }

    @SuppressWarnings("unchecked")
    default List<HippoNode> getNodes() {
        return relax(n -> {
            if (n.hasNodes()) {
                final NodeIterator nI = n.getNodes();
                final Spliterator<Node> sI = spliterator(nI, nI.getSize(), NONNULL | SIZED);
                return stream(sI, false);
            } else {
                return Stream.<Node>empty();
            }
        }, get(), RuntimeRepositoryException::new).map(n -> apply(n)).collect(toList());
    }

    default Optional<Property> getProperty(String name) {
        return relax(n -> n.hasProperty(name)
                        ? Optional.of(n.getProperty(name))
                        : Optional.empty(),
                get(), RuntimeRepositoryException::new
        );
    }

    @SuppressWarnings("unchecked")
    default List<Property> getProperties() {
        return relax(n -> {
            if (n.hasProperties()) {
                final PropertyIterator pI = n.getProperties();
                final Spliterator<Property> sI = spliterator(pI, pI.getSize(), NONNULL | SIZED);
                return stream(sI, false).collect(toList());
            } else {
                return Collections.emptyList();
            }
        }, get(), RuntimeRepositoryException::new);
    }

}
