package nl.meg;

import java.io.IOException;
import java.io.StringReader;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.commons.cnd.CndImporter;
import org.apache.jackrabbit.commons.cnd.ParseException;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractOakIntegrationTest {

    private static Repository REPOSITORY;
    private static Credentials ADMIN;

    protected Logger log;

    @BeforeAll
    static void setUp() {
        REPOSITORY = new Jcr(new Oak()).createRepository();
        ADMIN = new SimpleCredentials("admin", "admin".toCharArray());
    }

    @BeforeEach
    void setLogger() {
        log = getLogger();
    }

    protected Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }

    protected final Session getSession(Credentials credentials) throws RepositoryException {
        return REPOSITORY.login(credentials);
    }

    protected final Session getAdminSession() throws RepositoryException {
        return getSession(ADMIN);
    }

    protected final void loadCnd(String cnd) {
        try {
            CndImporter.registerNodeTypes(new StringReader(cnd), getAdminSession());
        } catch (ParseException | IOException | RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}
