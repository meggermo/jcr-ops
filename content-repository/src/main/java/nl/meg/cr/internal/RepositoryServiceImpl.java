package nl.meg.cr.internal;

import aQute.bnd.annotation.component.Component;
import nl.meg.cr.Repository;
import nl.meg.cr.RepositoryService;

@Component(name = "RepositoryService")
public class RepositoryServiceImpl implements RepositoryService {

    private final NodeSupport nodeSupport;
    private final ValueSupport valueSupport;

    public RepositoryServiceImpl(NodeSupport nodeSupport, ValueSupport valueSupport) {
        this.nodeSupport = nodeSupport;
        this.valueSupport = valueSupport;
    }

    @Override
    public Repository create(javax.jcr.Repository delegate) {
        return new RepositoryImpl(delegate, nodeSupport, valueSupport);
    }
}
