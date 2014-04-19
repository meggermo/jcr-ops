package nl.meg.jcr;

import nl.meg.jcr.exception.RuntimeRepositoryException;

import javax.jcr.Item;
import javax.jcr.Session;
import java.util.Optional;
import java.util.function.Supplier;

import static nl.meg.function.FunctionAdapter.relax;

public interface HippoItem<E extends Item> extends Supplier<E> {

    default String getName() {
        return relax(Item::getName, get(), RuntimeRepositoryException::new);
    }

    default String getPath() {
        return relax(Item::getPath, get(), RuntimeRepositoryException::new);
    }

    default Session getSession() {
        return relax(Item::getSession, get(), RuntimeRepositoryException::new);
    }

    default int getDepth() {
        return relax(Item::getDepth, get(), RuntimeRepositoryException::new);
    }

    default boolean isModified() {
        return relax(Item::isModified, get(), RuntimeRepositoryException::new);
    }

    default boolean isNew() {
        return relax(Item::isNew, get(), RuntimeRepositoryException::new);
    }

    default boolean isNode() {
        return relax(Item::isNode, get(), RuntimeRepositoryException::new);
    }

    default boolean isSame(HippoItem<E> other) {
        return relax(i -> i.isSame(other.get()), get(), RuntimeRepositoryException::new);
    }

    default Item getAncestor(int depth) {
        return relax(i -> i.getAncestor(depth), get(), RuntimeRepositoryException::new);
    }

    Optional<HippoNode> getParent();
}
