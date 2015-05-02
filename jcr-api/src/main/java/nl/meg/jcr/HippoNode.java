package nl.meg.jcr;


import aQute.bnd.annotation.ProviderType;

import javax.jcr.Node;
import javax.jcr.nodetype.NodeType;
import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Stream;

@ProviderType
public interface HippoNode extends HippoItem<Node> {

    Integer getIndex();

    String getIdentifier();

    boolean isRoot();

    boolean isNodeType(String nodeTypeName);

    NodeType getPrimaryNodeType();

    NodeType[] getMixinNodeTypes();

    Optional<HippoNode> getNode(String name);

    Stream<HippoNode> getNodes();

    Optional<HippoProperty> getProperty(String name);

    Optional<String> getString(String name);

    Stream<String> getStrings(String name);

    <E extends Enum<E>> Optional<E> getEnum(String name, Class<E> enumType);

    <E extends Enum<E>> Stream<E> getEnums(String name, Class<E> enumType);

    boolean getBoolean(String name);

    Optional<Calendar> getDate(String name);

    Stream<HippoProperty> getProperties();
}
