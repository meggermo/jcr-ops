package nl.meg.jcr;

import java.util.Calendar;

public interface MutableHippoNode {

    HippoNode setPrimaryType(String primaryTypeName);

    HippoNode addMixinType(String mixinTypeName);

    HippoNode setString(String name, String... values);

    HippoNode setBoolean(String name, Boolean... values);

    HippoNode setLong(String name, Long... values);

    HippoNode setDate(String name, Calendar... values);

    <E extends Enum<E>> HippoNode setEnum(String name, E... values);

    HippoNode addNode(String name);
}
