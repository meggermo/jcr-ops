package nl.meg.jcr;

import javax.jcr.Item;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.Session;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static nl.meg.jcr.RepoFunctionInvoker.invoke;

public interface HippoItem<E extends Item> extends Supplier<E>, Function<Node, HippoNode> {

    default String getName() {
        return invoke(Item::getName, get());
    }

    default String getPath() {
        return invoke(Item::getPath, get());
    }

    default Session getSession() {
        return invoke(Item::getSession, get());
    }

    default int getDepth() {
        return invoke(Item::getDepth, get());
    }

    default boolean isModified() {
        return invoke(Item::isModified, get());
    }

    default boolean isNew() {
        return invoke(Item::isNew, get());
    }

    default boolean isNode() {
        return invoke(Item::isNode, get());
    }

    default boolean isSame(HippoItem<E> other) {
        return invoke(i -> i.isSame(other.get()), get());
    }

    default Item getAncestor(int depth) {
        return invoke(i -> i.getAncestor(depth), get());
    }

    default Optional<HippoNode> getParent() {
        return Optional.ofNullable(
                invoke(i -> {
                    try {
                        return apply(i.getParent());
                    } catch (ItemNotFoundException e) {
                        return null;
                    }
                }, get())
        );
    }
}
