package nl.meg.jcr.store;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

public interface JcrNamed {

    String name();

    static <E extends JcrNamed> List<E> loadEntities(Node node, JcrRepo<E> repo) throws RepositoryException {
        final var entities = new ArrayList<E>();
        for (NodeIterator it = node.getNodes(); it.hasNext(); ) {
            entities.add(repo.load(it.nextNode()));
        }
        return entities;
    }

    static <E extends JcrNamed> void saveEntities(Node node, String namePattern, List<E> entities, JcrRepo<E> repo) throws RepositoryException {
        final var names = new HashSet<>();
        for (final E entity : entities) {
            if (!node.hasNode(entity.name())) {
                node.addNode(entity.name(), repo.nodeType());
            }
            names.add(entity.name());
            repo.save(node.getNode(entity.name()), entity);
        }
        for (NodeIterator it = node.getNodes(namePattern); it.hasNext(); ) {
            final Node n = it.nextNode();
            if (!names.contains(n.getName())) {
                n.remove();
            }
        }
    }

}
