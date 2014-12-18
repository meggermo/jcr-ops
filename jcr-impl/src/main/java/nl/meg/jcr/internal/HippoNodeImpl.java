package nl.meg.jcr.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.HippoValue;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.util.Calendar;
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
        return invoke(n -> {
            try {
                return Optional.of(node(n.getNode(name)));
            } catch (ItemNotFoundException | PathNotFoundException e) {
                return Optional.<HippoNode>empty();
            }
        });
    }

    @Override
    public Stream<HippoNode> getNodes() {
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
        return invoke(n -> {
            try {
                return Optional.of(property(n.getProperty(name)));
            } catch (PathNotFoundException e) {
                return Optional.<HippoProperty>empty();
            }
        });
    }

    @Override
    public Stream<HippoProperty> getProperties() {
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

    @Override
    public Optional<String> getString(String name) {
        return getValue(name).map(HippoValue::getString);
    }

    @Override
    public <E extends Enum<E>> Optional<E> getEnum(String name, Class<E> enumType) {
        return getString(name).map(v -> Enum.valueOf(enumType, v));
    }

    @Override
    public boolean getBoolean(String name) {
        return getValue(name).map(HippoValue::getBoolean).orElse(false);
    }

    @Override
    public Optional<Calendar> getDate(String name) {
        return getValue(name).map(HippoValue::getDate);
    }

    private Optional<HippoValue> getValue(String propertyName) {
        return getProperty(propertyName)
                .map(HippoProperty::getValue)
                .orElse(Optional.<HippoValue>empty());
    }

    @Override
    public Stream<String> getStrings(String name) {
        return getValues(name).map(HippoValue::getString);
    }

    @Override
    public <E extends Enum<E>> Stream<E> getEnums(String name, Class<E> enumType) {
        return getValues(name).map(v -> v.getEnum(enumType));
    }

    private Stream<HippoValue> getValues(String propertyName) {
        return getProperty(propertyName).map(HippoProperty::getValues).orElse(Stream.empty());
    }
}
