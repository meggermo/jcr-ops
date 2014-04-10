package nl.meg.jcr.function.internal;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import nl.meg.jcr.INode;
import nl.meg.jcr.function.NodeFunctions;

import javax.jcr.Property;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.util.Iterator;
import java.util.Optional;

final class NodeFunctionsImpl implements NodeFunctions {

    @Override
    public Function<INode, Session> getSession() {
        return GET_SESSION;
    }

    @Override
    public Function<INode, String> getName() {
        return GET_NAME;
    }

    @Override
    public Function<INode, String> getPath() {
        return GET_PATH;
    }

    @Override
    public Function<INode, String> getIdentifier() {
        return GET_ID;
    }

    @Override
    public Function<INode, NodeType> getPrimaryNodeType() {
        return GET_PRIMARY_NODE_TYPE;
    }

    @Override
    public Function<INode, Iterator<NodeType>> getMixinNodeTypes() {
        return GET_MIXIN_NODE_TYPES;
    }

    @Override
    public Function<INode, Integer> getIndex() {
        return GET_INDEX;
    }

    @Override
    public Function<INode, Optional<INode>> getParent() {
        return GET_PARENT;
    }

    @Override
    public Function<INode, Iterator<INode>> getNodes() {
        return GET_NODES;
    }

    @Override
    public Function<INode, Optional<Property>> getProperty(final String name) {
        return new Function<INode, Optional<Property>>() {
            @Override
            public Optional<Property> apply(INode node) {
                return node.getProperty(name);
            }
        };
    }

    @Override
    public Function<INode, Iterator<Property>> getProperties() {
        return GET_PROPERTIES;
    }

    private static final Function<INode, Session> GET_SESSION = new Function<INode, Session>() {
        @Override
        public Session apply(INode node) {
            return node.getSession();
        }
    };

    private static final Function<INode, String> GET_NAME = new Function<INode, String>() {
        @Override
        public String apply(INode node) {
            return node.getName();
        }
    };

    private static final Function<INode, String> GET_PATH = new Function<INode, String>() {
        @Override
        public String apply(INode node) {
            return node.getPath();
        }
    };

    private static final Function<INode, String> GET_ID = new Function<INode, String>() {
        @Override
        public String apply(INode node) {
            return node.getIdentifier();
        }
    };

    private static final Function<INode, NodeType> GET_PRIMARY_NODE_TYPE = new Function<INode, NodeType>() {
        @Override
        public NodeType apply(INode node) {
            return node.getPrimaryNodeType();
        }
    };

    private static final Function<INode, Iterator<Property>> GET_PROPERTIES = new Function<INode, Iterator<Property>>() {
        @Override
        public Iterator<Property> apply(INode node) {

            return node.getProperties();
        }
    };

    private static Function<INode, Integer> GET_INDEX = new Function<INode, Integer>() {
        @Override
        public Integer apply(INode node) {
            return node.getIndex();
        }
    };

    private static Function<INode, Optional<INode>> GET_PARENT = new Function<INode, Optional<INode>>() {
        @Override
        public Optional<INode> apply(INode node) {
            return node.getParent();
        }
    };

    private static Function<INode, Iterator<INode>> GET_NODES = new Function<INode, Iterator<INode>>() {
        @Override
        public Iterator<INode> apply(INode node) {
            return node.getNodes();
        }
    };

    private static Function<INode, Iterator<NodeType>> GET_MIXIN_NODE_TYPES = new Function<INode, Iterator<NodeType>>() {
        @Override
        public Iterator<NodeType> apply(INode node) {
            return Iterators.forArray(node.getMixinNodeTypes());
        }
    };
}
