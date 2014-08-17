package nl.meg.jcr.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoProperty;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.nodetype.NodeType;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Stream;

import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.SIZED;
import static java.util.Spliterators.spliterator;
import static java.util.stream.StreamSupport.stream;

final class HippoNodeImpl extends AbstractHippoItem<Node> implements HippoNode {

    HippoNodeImpl(Node node) {
        super(node);
    }

    public Integer getIndex() {
        return invoke(Node::getIndex);
    }

    public String getIdentifier() {
        return invoke(Node::getIdentifier);
    }

    public boolean isRoot() {
        return invoke(n -> n.getSession().getRootNode().isSame(get()));
    }

    public boolean isNodeType(String nodeTypeName) {
        return invoke(n -> n.isNodeType(nodeTypeName));
    }

    public NodeType getPrimaryNodeType() {
        return invoke(Node::getPrimaryNodeType);
    }

    public NodeType[] getMixinNodeTypes() {
        return invoke(Node::getMixinNodeTypes);
    }

    @Override
    public Optional<HippoNode> getNode(String name) {
        return invoke(n -> n.hasNode(name)
                ? Optional.of(node(n.getNode(name)))
                : Optional.empty());
    }

    @Override
    public Stream<HippoNode> getNodesAsStream() {
        return invoke(n -> {
            if (n.hasNodes()) {
                final NodeIterator nI = n.getNodes();
                @SuppressWarnings("unchecked")
                final Spliterator<Node> sI = spliterator(nI, nI.getSize(), NONNULL | SIZED);
                return stream(sI, false);
            } else {
                return Stream.<Node>empty();
            }
        }).map(this::node);
    }

    @Override
    public Optional<HippoProperty> getProperty(String name) {
        return invoke(n -> n.hasProperty(name)
                ? Optional.of(property(n.getProperty(name)))
                : Optional.empty());
    }

    @Override
    public Stream<HippoProperty> getPropertiesAsStream() {
        return invoke(n -> {
            if (n.hasProperties()) {
                final PropertyIterator pI = n.getProperties();
                @SuppressWarnings("unchecked")
                final Spliterator<Property> sI = spliterator(pI, pI.getSize(), NONNULL | SIZED);
                return stream(sI, false);
            } else {
                return Stream.<Property>empty();
            }
        }).map(this::property);
    }

}
