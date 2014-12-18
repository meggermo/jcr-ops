package nl.meg.jcr;

import java.util.Calendar;

public interface MutableHippoNode {

    HippoNode setPrimaryType(String primaryTypeName);

    HippoNode addMixinType(String mixinTypeName);

    HippoNode setProperty(String name, String... values);

    HippoNode setProperty(String name, Boolean... values);

    HippoNode setProperty(String name, Long... values);

    HippoNode setProperty(String name, Calendar... values);

    <E extends Enum<E>> HippoNode setProperty(String name, E... values);
}
