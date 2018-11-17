package nl.meg.jcr;


import java.util.Calendar;
import java.util.function.Supplier;

import javax.jcr.Node;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface MutableHippoNode extends Supplier<Node> {

    MutableHippoNode setPrimaryType(String primaryTypeName);

    MutableHippoNode addMixinType(String mixinTypeName);

    MutableHippoNode setString(String name, String... values);

    MutableHippoNode setBoolean(String name, Boolean... values);

    MutableHippoNode setLong(String name, Long... values);

    MutableHippoNode setDate(String name, Calendar... values);

    <E extends Enum<E>> MutableHippoNode setEnum(String name, E... values);

    HippoNode addNode(String name);
}
