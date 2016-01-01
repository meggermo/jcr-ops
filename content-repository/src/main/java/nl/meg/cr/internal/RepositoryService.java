package nl.meg.cr.internal;

import aQute.bnd.annotation.ProviderType;
import nl.meg.cr.Repository;

@ProviderType
public interface RepositoryService {

    Repository create(javax.jcr.Repository delegate);
}
