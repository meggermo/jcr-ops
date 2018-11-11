package nl.meg.jcr.function;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

public final class JcrPropertyFactory {

    public static JcrPropertyImpl<String> ofString(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> node.getProperty(name).getString(),
                (n, v) -> n.setProperty(name, v));
    }

    public static JcrPropertyImpl<Optional<String>> ofStringOption(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> getOption(node, name, Property::getString),
                setOption(name, FROM_STRING)
        );
    }

    public static JcrPropertyImpl<List<String>> ofStringList(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> getValues(node.getProperty(name).getValues(), Value::getString),
                setValues(name, FROM_STRING));
    }

    public static JcrPropertyImpl<Optional<List<String>>> ofStringListOption(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> getOption(node, name, p -> getValues(p.getValues(), Value::getString)),
                setListOption(name, FROM_STRING)
        );
    }


    public static JcrPropertyImpl<Long> ofLong(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> node.getProperty(name).getLong(),
                (n, v) -> n.setProperty(name, v));
    }

    public static JcrPropertyImpl<Optional<Long>> ofLongOption(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> getOption(node, name, Property::getLong),
                setOption(name, FROM_LONG));
    }

    public static JcrPropertyImpl<List<Long>> ofLongList(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> getValues(node.getProperty(name).getValues(), Value::getLong),
                setValues(name, FROM_LONG));
    }

    public static JcrPropertyImpl<Optional<List<Long>>> ofLongListOption(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> getOption(node, name, p -> getValues(p.getValues(), Value::getLong)),
                setListOption(name, FROM_LONG)
        );
    }


    public static JcrPropertyImpl<Boolean> ofBoolean(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> node.getProperty(name).getBoolean(),
                (n, v) -> n.setProperty(name, v));
    }

    public static JcrPropertyImpl<Optional<Boolean>> ofBooleanOption(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> getOption(node, name, Property::getBoolean),
                setOption(name, FROM_BOOLEAN));
    }

    public static JcrPropertyImpl<List<Boolean>> ofBooleanList(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> getValues(node.getProperty(name).getValues(), Value::getBoolean),
                setValues(name, FROM_BOOLEAN));
    }

    public static JcrPropertyImpl<Optional<List<Boolean>>> ofBooleanListOption(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> getOption(node, name, p -> getValues(p.getValues(), Value::getBoolean)),
                setListOption(name, FROM_BOOLEAN)
        );
    }


    public static JcrPropertyImpl<Instant> ofInstant(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> node.getProperty(name).getDate().toInstant(),
                (n, v) -> {
                    final Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(v.toEpochMilli());
                    n.setProperty(name, c);
                    return null;
                });
    }

    public static JcrPropertyImpl<Optional<Instant>> ofInstantOption(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> getOption(node, name, p -> p.getDate().toInstant()),
                setOption(name, FROM_INSTANT));
    }

    public static JcrPropertyImpl<List<Instant>> ofInstantList(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> getValues(node.getProperty(name).getValues(), v -> v.getDate().toInstant()),
                setValues(name, FROM_INSTANT));
    }

    public static JcrPropertyImpl<Optional<List<Instant>>> ofInstantListOption(String name) {
        return new JcrPropertyImpl<>(
                name,
                node -> getOption(node, name, p -> getValues(p.getValues(), v -> v.getDate().toInstant())),
                setListOption(name, FROM_INSTANT)
        );
    }

    private static <T> Optional<T> getOption(Node node, String name, JcrFunction<Property, T> getter) throws RepositoryException {
        if (node.hasProperty(name)) {
            return Optional.of(getter.apply(node.getProperty(name)));
        }
        return Optional.empty();
    }

    private static final JcrBiFunction<Node, String, Value> FROM_STRING =
            (node, value) -> node.getSession().getValueFactory().createValue(value);

    private static final JcrBiFunction<Node, Long, Value> FROM_LONG =
            (node, value) -> node.getSession().getValueFactory().createValue(value);

    private static final JcrBiFunction<Node, Boolean, Value> FROM_BOOLEAN =
            (node, value) -> node.getSession().getValueFactory().createValue(value);

    private static final JcrBiFunction<Node, Instant, Value> FROM_INSTANT =
            (node, value) -> {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(value.toEpochMilli());
                return node.getSession().getValueFactory().createValue(calendar);
            };


    private static <T> JcrBiFunction<Node, Optional<T>, Optional<T>> setOption(String name, JcrBiFunction<Node, T, Value> f) {
        return (node, value) -> {
            if (value.isPresent()) {
                node.setProperty(name, f.apply(node, value.get()));
            } else if (node.hasProperty(name)) {
                node.getProperty(name).remove();
            }
            return value;
        };
    }

    private static <T> JcrBiFunction<Node, Optional<List<T>>, ?> setListOption(String name, JcrBiFunction<Node, T, Value> f) {
        return (node, values) -> {
            if (values.isPresent()) {
                setValues(name, f).apply(node, values.get());
            } else if (node.hasProperty(name)) {
                node.getProperty(name).remove();
            }
            return null;
        };
    }

    private static <T> List<T> getValues(Value[] values, JcrFunction<Value, T> f) throws RepositoryException {
        final List<T> valueList = new ArrayList<>(values.length);
        for (Value v : values) {
            valueList.add(f.apply(v));
        }
        return valueList;
    }

    private static <T> JcrBiFunction<Node, List<T>, ?> setValues(String name, JcrBiFunction<Node, T, Value> f) {
        return (node, values) -> {
            final Value[] vs = new Value[values.size()];
            for (int i = 0; i < values.size(); i++) {
                vs[i] = f.apply(node, values.get(i));
            }
            node.setProperty(name, vs);
            return null;
        };
    }

    private static final class JcrPropertyImpl<V> implements nl.meg.jcr.function.JcrProperty<V> {

        private final String name;
        private final JcrFunction<Node, V> valueGetter;
        private final JcrBiFunction<Node, V, ?> valueSetter;

        JcrPropertyImpl(String name, JcrFunction<Node, V> valueGetter, JcrBiFunction<Node, V, ?> valueSetter) {
            this.name = name;
            this.valueGetter = valueGetter;
            this.valueSetter = valueSetter;
        }

        public String getName() {
            return name;
        }

        public V getValue(Node node) throws RepositoryException {
            return valueGetter.apply(node);
        }

        public JcrPropertyImpl<V> setValue(final Node node, final V value) throws RepositoryException {
            valueSetter.apply(node, value);
            return this;
        }

        @Override
        public String toString() {
            return nl.meg.jcr.function.JcrProperty.class.getSimpleName() +
                    "[" + "name=" + name + "]";
        }

    }

}
