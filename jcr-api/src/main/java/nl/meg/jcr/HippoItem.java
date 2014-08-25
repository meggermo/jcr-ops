package nl.meg.jcr;

import javax.jcr.Item;
import javax.jcr.Session;
import java.util.Optional;
import java.util.function.Supplier;

public interface HippoItem<E extends Item> extends Supplier<E> {

    String getName();

    String getPath();

    Session getSession();

    int getDepth();

    boolean isModified();

    boolean isNew();

    boolean isNode();

    boolean isSame(HippoItem<E> other);

    Item getAncestor(int depth);

    Optional<HippoNode> getParent();
}
