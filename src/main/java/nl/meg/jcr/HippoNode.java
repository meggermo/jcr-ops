package nl.meg.jcr;


import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.nodetype.NodeType;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyIterator;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.SIZED;
import static java.util.Spliterators.spliterator;
import static java.util.stream.StreamSupport.stream;
import static nl.meg.jcr.RepoFunctionInvoker.invoke;

public interface HippoNode extends HippoItem<Node> {

    default Integer getIndex() {
        return invoke(Node::getIndex, get());
    }

    default String getIdentifier() {
        return invoke(Node::getIdentifier, get());
    }

    default boolean isRoot() {
        return invoke(n -> n.getSession().getRootNode().isSame(get()), get());
    }

    default Optional<HippoNode> getNode(String name) {
        return invoke(n -> n.hasNode(name)
                        ? Optional.of(apply(n.getNode(name)))
                        : Optional.empty(), get()
        );
    }

    default Optional<Property> getProperty(String name) {
        return invoke(n -> n.hasProperty(name)
                        ? Optional.of(n.getProperty(name))
                        : Optional.empty(), get()
        );
    }

    @SuppressWarnings("unchecked")
    default Iterator<Property> getProperties() {
        return invoke(n -> n.hasProperties()
                        ? n.getProperties()
                        : emptyIterator(), get()
        );
    }

    @SuppressWarnings("unchecked")
    default Stream<Property> getPropertyStream() {
        return invoke(n -> {
            if (n.hasProperties()) {
                final PropertyIterator pI = n.getProperties();
                final Spliterator<Property> sI = spliterator(pI, pI.getSize(), NONNULL | SIZED);
                return stream(sI, false);
            } else {
                return Stream.<Property>empty();
            }
        }, get());
    }

    default boolean isNodeType(String nodeTypeName) {
        return invoke(n -> n.isNodeType(nodeTypeName), get());
    }

    default NodeType getPrimaryNodeType() {
        return invoke(Node::getPrimaryNodeType, get());
    }

    default NodeType[] getMixinNodeTypes() {
        return invoke(Node::getMixinNodeTypes, get());
    }

    @SuppressWarnings("unchecked")
    default Iterator<HippoNode> getNodes() {
        return getNodeStream().collect(Collectors.toList()).iterator();
    }

    @SuppressWarnings("unchecked")
    default Stream<HippoNode> getNodeStream() {
        return invoke(n -> {
            if (n.hasNodes()) {
                final NodeIterator nI = n.getNodes();
                final Spliterator<Node> sI = spliterator(nI, nI.getSize(), NONNULL | SIZED);
                return stream(sI, false);
            } else {
                return Stream.<Node>empty();
            }
        }, get()).map(n -> apply(n));
    }
}
