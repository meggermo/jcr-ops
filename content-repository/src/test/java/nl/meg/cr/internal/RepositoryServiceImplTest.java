package nl.meg.cr.internal;

import nl.meg.cr.RepositoryService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RepositoryServiceImplTest extends AbstractMockitoTest {

    @Mock
    private Repository r;
    private RepositoryService service;

    @Before
    public void setUp() {
        this.service = new RepositoryServiceImpl();
    }

    @Test
    public void testCreate() {
        assertThat(service.create(r), is(new RepositoryImpl(r)));
    }

}
