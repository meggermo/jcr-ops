package nl.meg.jcr.function;

import com.google.common.base.Function;

import javax.jcr.nodetype.NodeType;

public interface NodeTypeFunctions {

    Function<NodeType, String> getName();

}
