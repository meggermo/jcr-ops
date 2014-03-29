package nl.meg.jcr.function.internal;

import com.google.common.base.Function;
import nl.meg.jcr.function.NodeTypeFunctions;

import javax.jcr.nodetype.NodeType;

final class NodeTypeFunctionsImpl implements NodeTypeFunctions {

    @Override
    public Function<NodeType, String> getName() {
        return GET_NAME;
    }

    private static Function<NodeType, String> GET_NAME = new Function<NodeType, String>() {
        @Override
        public String apply(NodeType nodeType) {
            return nodeType.getName();
        }
    };

}
