package nl.meg.jcr.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoProperty;
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

final class HippoNodeImpl extends AbstractHippoItem<Node> implements HippoNode {

    HippoNodeImpl(Node node) {
        super(node);
    }

    public Integer getIndex() {
        return relax(Node::getIndex, get(), RuntimeRepositoryException::new);
    }

    public String getIdentifier() {
        return relax(Node::getIdentifier, get(), RuntimeRepositoryException::new);
    }

    public boolean isRoot() {
        return relax(n -> n.getSession().getRootNode().isSame(get()), get(), RuntimeRepositoryException::new);
    }

    public boolean isNodeType(String nodeTypeName) {
        return relax(n -> n.isNodeType(nodeTypeName), get(), RuntimeRepositoryException::new);
    }

    public NodeType getPrimaryNodeType() {
        return relax(Node::getPrimaryNodeType, get(), RuntimeRepositoryException::new);
    }

    public NodeType[] getMixinNodeTypes() {
        return relax(Node::getMixinNodeTypes, get(), RuntimeRepositoryException::new);
    }

    @Override
    public Optional<HippoNode> getNode(String name) {
        return relax(n -> n.hasNode(name)
                        ? Optional.of(node(n.getNode(name)))
                        : Optional.empty(),
                get(),
                RuntimeRepositoryException::new
        );
    }

    @Override
    public List<HippoNode> getNodes() {
        return relax(n -> {
            if (n.hasNodes()) {
                final NodeIterator nI = n.getNodes();
                @SuppressWarnings("unchecked")
                final Spliterator<Node> sI = spliterator(nI, nI.getSize(), NONNULL | SIZED);
                return stream(sI, false);
            } else {
                return Stream.<Node>empty();
            }
        }, get(), RuntimeRepositoryException::new).map(this::node).collect(toList());
    }

    @Override
    public Optional<HippoProperty> getProperty(String name) {
        return relax(n -> n.hasProperty(name)
                        ? Optional.of(property(n.getProperty(name)))
                        : Optional.empty(),
                get(), RuntimeRepositoryException::new
        );
    }

    @Override
    public List<HippoProperty> getProperties() {
        return relax(n -> {
            if (n.hasProperties()) {
                final PropertyIterator pI = n.getProperties();
                @SuppressWarnings("unchecked")
                final Spliterator<Property> sI = spliterator(pI, pI.getSize(), NONNULL | SIZED);
                return stream(sI, false).map(this::property).collect(toList());
            } else {
                return Collections.emptyList();
            }
        }, get(), RuntimeRepositoryException::new);
    }

}
