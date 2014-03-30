package nl.meg.jcr.function.internal;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterators;
import nl.meg.jcr.exception.RuntimeRepositoryException;
import nl.meg.jcr.function.NodeFunctions;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.util.Iterator;

import static java.util.Collections.emptyIterator;

final class NodeFunctionsImpl implements NodeFunctions {

    @Override
    public Function<Node, Session> getSession() {
        return GET_SESSION;
    }

    @Override
    public Function<Node, String> getName() {
        return GET_NAME;
    }

    @Override
    public Function<Node, String> getPath() {
        return GET_PATH;
    }

    @Override
    public Function<Node, String> getIdentifier() {
        return GET_ID;
    }

    @Override
    public Function<Node, NodeType> getPrimaryNodeType() {
        return GET_PRIMARY_NODE_TYPE;
    }

    @Override
    public Function<Node, Iterator<NodeType>> getMixinNodeTypes() {
        return GET_MIXIN_NODE_TYPES;
    }

    @Override
    public Function<Node, Integer> getIndex() {
        return GET_INDEX;
    }

    @Override
    public Function<Node, Optional<Node>> getParent() {
        return GET_PARENT;
    }

    @Override
    public Function<Node, Iterator<Node>> getNodes() {
        return GET_NODES;
    }

    @Override
    public Function<Node, Optional<Property>> getProperty(final String name) {
        return new Function<Node, Optional<Property>>() {
            @Override
            public Optional<Property> apply(Node node) {
                try {
                    if (node.hasProperty(name)) {
                        return Optional.of(node.getProperty(name));
                    } else {
                        return Optional.absent();
                    }
                } catch (RepositoryException e) {
                    throw new RuntimeRepositoryException(e);
                }
            }
        };
    }

    @Override
    public Function<Node, Iterator<Property>> getProperties() {
        return GET_PROPERTIES;
    }

    private static final Function<Node, Session> GET_SESSION = new Function<Node, Session>() {
        @Override
        public Session apply(Node node) {
            try {
                return node.getSession();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static final Function<Node, String> GET_NAME = new Function<Node, String>() {
        @Override
        public String apply(Node node) {
            try {
                return node.getName();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static final Function<Node, String> GET_PATH = new Function<Node, String>() {
        @Override
        public String apply(Node node) {
            try {
                return node.getPath();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static final Function<Node, String> GET_ID = new Function<Node, String>() {
        @Override
        public String apply(Node node) {
            try {
                return node.getIdentifier();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static final Function<Node, NodeType> GET_PRIMARY_NODE_TYPE = new Function<Node, NodeType>() {
        @Override
        public NodeType apply(Node node) {
            try {
                return node.getPrimaryNodeType();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static final Function<Node, Iterator<Property>> GET_PROPERTIES = new Function<Node, Iterator<Property>>() {
        @Override
        public Iterator<Property> apply(Node node) {
            try {
                return node.hasProperties() ? node.getProperties() : emptyIterator();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static Function<Node, Integer> GET_INDEX = new Function<Node, Integer>() {
        @Override
        public Integer apply(Node node) {
            try {
                return node.getIndex();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static Function<Node, Optional<Node>> GET_PARENT = new Function<Node, Optional<Node>>() {
        @Override
        public Optional<Node> apply(Node node) {
            try {
                return Optional.of(node.getParent());
            } catch (ItemNotFoundException e) {
                return Optional.absent();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static Function<Node, Iterator<Node>> GET_NODES = new Function<Node, Iterator<Node>>() {
        @Override
        public Iterator<Node> apply(Node node) {
            try {
                return node.getNodes();
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };

    private static Function<Node, Iterator<NodeType>> GET_MIXIN_NODE_TYPES = new Function<Node, Iterator<NodeType>>() {
        @Override
        public Iterator<NodeType> apply(Node node) {
            try {
                return Iterators.forArray(node.getMixinNodeTypes());
            } catch (RepositoryException e) {
                throw new RuntimeRepositoryException(e);
            }
        }
    };
}
