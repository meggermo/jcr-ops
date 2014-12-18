package nl.meg.jcr.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.MutableHippoNode;

import javax.jcr.Node;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import java.util.Calendar;
import java.util.stream.Stream;

final class MutableHippoNodeImpl extends AbstractHippoItem<Node> implements MutableHippoNode {

    public MutableHippoNodeImpl(Node node) {
        super(node);
    }

    @Override
    public HippoNode setPrimaryType(String primaryTypeName) {
        return invoke(n -> {
            n.setPrimaryType(primaryTypeName);
            return node(n);
        });
    }

    @Override
    public HippoNode addMixinType(String mixin) {
        return invoke(n -> {
            n.addMixin(mixin);
            return node(n);
        });
    }

    @Override
    public HippoNode setProperty(String name, String... values) {
        return invoke(n -> {
            switch (values.length) {
                case 1:
                    n.setProperty(name, values[0]);
                    return node(n);
                default:
                    n.setProperty(name, values);
                    return node(n);
            }
        });
    }

    @Override
    public HippoNode setProperty(String name, Boolean... values) {
        return invoke(n -> {
            switch (values.length) {
                case 1:
                    n.setProperty(name, values[0]);
                    return node(n);
                default:
                    n.setProperty(name, booleanValues(values));
                    return node(n);
            }
        });
    }

    @Override
    public HippoNode setProperty(String name, Long... values) {
        return invoke(n -> {
            switch (values.length) {
                case 1:
                    n.setProperty(name, values[0]);
                    return node(n);
                default:
                    n.setProperty(name, longValues(values));
                    return node(n);
            }
        });
    }

    @Override
    public HippoNode setProperty(String name, Calendar... values) {
        return invoke(n -> {
            switch (values.length) {
                case 1:
                    n.setProperty(name, values[0]);
                    return node(n);
                default:
                    n.setProperty(name, calendarValues(values));
                    return node(n);
            }
        });
    }

    @Override
    public <E extends Enum<E>> HippoNode setProperty(String name, E... values) {
        return invoke(n -> {
            switch (values.length) {
                case 1:
                    n.setProperty(name, values[0].name());
                    return node(n);
                default:
                    n.setProperty(name, Stream.of(values).map(Enum::name).<String>toArray(String[]::new));
                    return node(n);
            }
        });
    }

    private Value[] booleanValues(Boolean... values) {
        return invoke(n -> {
            final ValueFactory factory = n.getSession().getValueFactory();
            return Stream.of(values).map(b -> factory.createValue(b)).toArray(Value[]::new);
        });
    }

    private Value[] longValues(Long... values) {
        return invoke(n -> {
            final ValueFactory factory = n.getSession().getValueFactory();
            return Stream.of(values).map(b -> factory.createValue(b)).toArray(Value[]::new);
        });
    }

    private Value[] calendarValues(Calendar... values) {
        return invoke(n -> {
            final ValueFactory factory = n.getSession().getValueFactory();
            return Stream.of(values).map(b -> factory.createValue(b)).toArray(size -> new Value[size]);
        });
    }
}
