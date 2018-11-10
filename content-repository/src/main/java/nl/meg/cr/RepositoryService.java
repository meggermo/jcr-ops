package nl.meg.cr;


import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface RepositoryService {

    Repository create(javax.jcr.Repository delegate);
}
