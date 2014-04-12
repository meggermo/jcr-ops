package nl.meg.jcr;


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

    default boolean isNodeType(String nodeTypeName) {
        return invoke(n -> n.isNodeType(nodeTypeName), get());
    }

    default NodeType getPrimaryNodeType() {
        return invoke(Node::getPrimaryNodeType, get());
    }

    default NodeType[] getMixinNodeTypes() {
        return invoke(Node::getMixinNodeTypes, get());
    }

    default Optional<HippoNode> getNode(String name) {
        return invoke(n -> n.hasNode(name)
                        ? Optional.of(apply(n.getNode(name)))
                        : Optional.empty(),
                get()
        );
    }

    @SuppressWarnings("unchecked")
    default List<HippoNode> getNodes() {
        return invoke(n -> {
            if (n.hasNodes()) {
                final NodeIterator nI = n.getNodes();
                final Spliterator<Node> sI = spliterator(nI, nI.getSize(), NONNULL | SIZED);
                return stream(sI, false);
            } else {
                return Stream.<Node>empty();
            }
        }, get()).map(n -> apply(n)).collect(toList());
    }

    default Optional<Property> getProperty(String name) {
        return invoke(n -> n.hasProperty(name)
                        ? Optional.of(n.getProperty(name))
                        : Optional.empty(),
                get()
        );
    }

    @SuppressWarnings("unchecked")
    default List<Property> getProperties() {
        return invoke(n -> {
            if (n.hasProperties()) {
                final PropertyIterator pI = n.getProperties();
                final Spliterator<Property> sI = spliterator(pI, pI.getSize(), NONNULL | SIZED);
                return stream(sI, false).collect(toList());
            } else {
                return Collections.emptyList();
            }
        }, get());
    }

}
