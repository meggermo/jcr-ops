package nl.meg.jcr;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.collect.ImmutableList.copyOf;
import static java.util.Collections.emptyIterator;
import static nl.meg.jcr.RepoFunctionInvoker.invoke;

public interface INode extends Supplier<Node>, Function<Node, INode> {

    default Integer getIndex() {
        return invoke(get(), Node::getIndex);
    }

    default String getIdentifier() {
        return invoke(get(), Node::getIdentifier);
    }

    default String getName() {
        return invoke(get(), Node::getName);
    }

    default String getPath() {
        return invoke(get(), Node::getPath);
    }

    default Session getSession() {
        return invoke(get(), Node::getSession);
    }

    default boolean isRoot() {
        return invoke(get(), n -> n.getSession().getRootNode().isSame(get()));
    }

    default boolean isSame(INode node) {
        return invoke(get(), n -> n.isSame(node.get()));
    }

    default Optional<INode> getNode(String name) {
        return invoke(get(),
                n -> n.hasNode(name)
                        ? Optional.of(apply(n.getNode(name)))
                        : Optional.empty()
        );
    }

    default Optional<Property> getProperty(String name) {
        return invoke(get(),
                n -> n.hasProperty(name)
                        ? Optional.of(n.getProperty(name))
                        : Optional.empty()
        );
    }

    @SuppressWarnings("unchecked")
    default Iterator<Property> getProperties() {
        return invoke(get(),
                n -> n.hasProperties()
                        ? n.getProperties()
                        : emptyIterator()
        );
    }

    default boolean isNodeType(String nodeTypeName) {
        return invoke(get(), n -> n.isNodeType(nodeTypeName));
    }

    default NodeType getPrimaryNodeType() {
        return invoke(get(), Node::getPrimaryNodeType);
    }

    default NodeType[] getMixinNodeTypes() {
        return invoke(get(), Node::getMixinNodeTypes);
    }

    @SuppressWarnings("unchecked")
    default Iterator<INode> getNodes() {
        return invoke(get(),
                n -> n.hasNodes()
                        ? copyOf(n.getNodes()).stream().map(this).iterator()
                        : emptyIterator()
        );
    }

    default Optional<INode> getParent() {
        return invoke(get(), n -> {
            try {
                return Optional.of(INode.this.apply(n.getParent()));
            } catch (ItemNotFoundException e) {
                return Optional.empty();
            }
        });
    }

}
