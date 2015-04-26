package nl.meg.jcr.internal;

import nl.meg.jcr.HippoEntityFactory;
import nl.meg.jcr.HippoNode;
import nl.meg.jcr.MutableHippoNode;

import javax.jcr.Node;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import java.util.Calendar;
import java.util.stream.Stream;

final class MutableHippoNodeImpl extends AbstractHippoItem<Node> implements MutableHippoNode {

    public MutableHippoNodeImpl(Node node, HippoEntityFactory hippoEntityFactory) {
        super(node, hippoEntityFactory);
    }

    @Override
    public MutableHippoNode setPrimaryType(String primaryTypeName) {
        return invoke(n -> {
            n.setPrimaryType(primaryTypeName);
            return factory().mutableNode(n);
        });
    }

    @Override
    public MutableHippoNode addMixinType(String mixin) {
        return invoke(n -> {
            n.addMixin(mixin);
            return factory().mutableNode(n);
        });
    }

    @Override
    public MutableHippoNode setString(String name, String... values) {
        return invoke(n -> {
            switch (values.length) {
                case 1:
                    n.setProperty(name, values[0]);
                    return factory().mutableNode(n);
                default:
                    n.setProperty(name, values);
                    return factory().mutableNode(n);
            }
        });
    }

    @Override
    public MutableHippoNode setBoolean(String name, Boolean... values) {
        return invoke(n -> {
            switch (values.length) {
                case 1:
                    n.setProperty(name, values[0]);
                    return factory().mutableNode(n);
                default:
                    n.setProperty(name, booleanValues(values));
                    return factory().mutableNode(n);
            }
        });
    }

    @Override
    public MutableHippoNode setLong(String name, Long... values) {
        return invoke(n -> {
            switch (values.length) {
                case 1:
                    n.setProperty(name, values[0]);
                    return factory().mutableNode(n);
                default:
                    n.setProperty(name, longValues(values));
                    return factory().mutableNode(n);
            }
        });
    }

    @Override
    public MutableHippoNode setDate(String name, Calendar... values) {
        return invoke(n -> {
            switch (values.length) {
                case 1:
                    n.setProperty(name, values[0]);
                    return factory().mutableNode(n);
                default:
                    n.setProperty(name, calendarValues(values));
                    return factory().mutableNode(n);
            }
        });
    }

    @Override
    public <E extends Enum<E>> MutableHippoNode setEnum(String name, E... values) {
        return invoke(n -> {
            switch (values.length) {
                case 1:
                    n.setProperty(name, values[0].name());
                    return factory().mutableNode(n);
                default:
                    n.setProperty(name, Stream.of(values).map(Enum::name).toArray(String[]::new));
                    return factory().mutableNode(n);
            }
        });
    }

    @Override
    public HippoNode addNode(String name) {
        return invoke(n -> factory().node(n.addNode(name)));
    }

    private Value[] booleanValues(Boolean... values) {
        return invoke(n -> {
            final ValueFactory factory = n.getSession().getValueFactory();
            return Stream.of(values).map(factory::createValue).toArray(Value[]::new);
        });
    }

    private Value[] longValues(Long... values) {
        return invoke(n -> {
            final ValueFactory factory = n.getSession().getValueFactory();
            return Stream.of(values).map(factory::createValue).toArray(Value[]::new);
        });
    }

    private Value[] calendarValues(Calendar... values) {
        return invoke(n -> {
            final ValueFactory factory = n.getSession().getValueFactory();
            return Stream.of(values).map(factory::createValue).toArray(Value[]::new);
        });
    }

}
