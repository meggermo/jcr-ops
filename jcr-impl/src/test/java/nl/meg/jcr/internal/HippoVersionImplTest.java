package nl.meg.jcr.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoVersion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionHistory;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class HippoVersionImplTest extends AbstractMockitoTest {

    @Mock
    private Version version;

    @Mock
    private VersionHistory versionHistory;

    @Mock
    private Node node;

    private HippoVersion hippoVersion;

    @Before
    public void setUp() {
        this.hippoVersion = new HippoVersionImpl(version);
    }

    @Test
    public void testGetContainingHistory() throws RepositoryException {
        when(version.getContainingHistory()).thenReturn(versionHistory);
        assertThat(hippoVersion.getContainingHistory(), is(notNullValue()));
    }

    @Test
    public void testGetCreated() throws RepositoryException {
        final Calendar created = Calendar.getInstance();
        when(version.getCreated()).thenReturn(created);
        assertThat(hippoVersion.getCreated(), is(created));
    }

    @Test
    public void testGetLinearSuccessor() throws RepositoryException {
        when(version.getLinearSuccessor()).thenReturn(version);
        assertThat(hippoVersion.getLinearSuccessor().get().get(), is(version));
    }

    @Test
    public void testGetLinearSuccessor_When_Absent() throws RepositoryException {
        when(version.getLinearSuccessor()).thenThrow(new VersionException());
        assertThat(hippoVersion.getLinearSuccessor().isPresent(), is(false));
    }

    @Test
    public void testGetSuccessors() throws RepositoryException {
        when(version.getSuccessors()).thenReturn(new Version[]{version});
        assertThat(hippoVersion.getSuccessors().count(), is(1L));
    }

    @Test
    public void testGetLinearPredecessor() throws RepositoryException {
        when(version.getLinearPredecessor()).thenReturn(version);
        assertThat(hippoVersion.getLinearPredecessor().get().get(), is(version));
    }

    @Test
    public void testGetLinearPredecessor_When_Absent() throws RepositoryException {
        when(version.getLinearPredecessor()).thenThrow(new VersionException());
        assertThat(hippoVersion.getLinearPredecessor().isPresent(), is(false));
    }

    @Test
    public void testGetPredecessors() throws RepositoryException {
        when(version.getPredecessors()).thenReturn(new Version[]{version});
        assertThat(hippoVersion.getPredecessors().count(), is(1L));
    }

    @Test
    public void testGetFrozenNode() throws RepositoryException {
        when(version.getFrozenNode()).thenReturn(node);
        assertThat(hippoVersion.getFrozenNode().get(), is(node));
    }
}
