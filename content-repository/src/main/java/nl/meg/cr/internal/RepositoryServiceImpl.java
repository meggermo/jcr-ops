package nl.meg.cr.internal;

import aQute.bnd.annotation.component.Component;
import nl.meg.cr.Repository;
import nl.meg.cr.RepositoryService;

@Component(name = "RepositoryService")
public class RepositoryServiceImpl implements RepositoryService {

    @Override
    public Repository create(javax.jcr.Repository delegate) {
        return new RepositoryImpl(delegate);
    }
}
