package nl.meg.cr;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface Repository {

    Session login(String username, String password) throws LoginException;
}
