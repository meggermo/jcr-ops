package nl.meg.jcr.function;

import javax.jcr.Node;

public interface JcrProperty<V> {

    V getValue();

    JcrBiFunction<Node, V, ?> valueSetter();
}
