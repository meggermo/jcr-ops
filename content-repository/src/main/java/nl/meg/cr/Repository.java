package nl.meg.cr;


import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface Repository {

    Session login(String username, String password) throws LoginException;
}
