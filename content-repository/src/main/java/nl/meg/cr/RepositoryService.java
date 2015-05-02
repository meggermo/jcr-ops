package nl.meg.cr;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface RepositoryService {

    Repository create(javax.jcr.Repository delegate);
}
