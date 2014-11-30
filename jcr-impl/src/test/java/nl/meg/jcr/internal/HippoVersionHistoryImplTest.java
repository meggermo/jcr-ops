package nl.meg.jcr.internal;

import nl.meg.AbstractMockitoTest;
import nl.meg.jcr.HippoVersionHistory;
import nl.meg.jcr.RuntimeRepositoryException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.version.LabelExistsVersionException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionHistory;
import java.util.Arrays;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class HippoVersionHistoryImplTest extends AbstractMockitoTest {

    @Mock
    private VersionHistory versionHistory;

    @Mock
    private Version version;

    @Mock
    private Node node;

    private HippoVersionHistory hvh;

    @Before
    public void setUp() {
        this.hvh = new HippoVersionHistoryImpl(versionHistory);
    }

    @Test
    public void testGetVersionableIdentifier() throws RepositoryException {
        when(versionHistory.getVersionableIdentifier()).thenReturn("test");
        assertThat(hvh.getVersionableIdentifier(), is("test"));
    }

    @Test
    public void testGetRootVersion() throws RepositoryException {
        when(versionHistory.getRootVersion()).thenReturn(version);
        assertThat(hvh.getRootVersion().get(), is(version));
    }

    @Test
    public void testGetAllLinearVersions() throws RepositoryException {
        when(versionHistory.getAllLinearVersions()).thenReturn(getVersionIterator(version));
        assertThat(hvh.getAllLinearVersions().count(), is(1L));
    }

    @Test
    public void testGetAllVersions() throws RepositoryException {
        when(versionHistory.getAllVersions()).thenReturn(getVersionIterator(version));
        assertThat(hvh.getAllVersions().count(), is(1L));
    }

    @Test
    public void testGetAllLinearFrozenNodes() throws RepositoryException {
        when(versionHistory.getAllLinearFrozenNodes()).thenReturn(getNodeIterator(node));
        assertThat(hvh.getAllLinearFrozenNodes().count(), is(1L));
    }

    @Test
    public void testGetAllFrozenNodes() throws RepositoryException {
        when(versionHistory.getAllFrozenNodes()).thenReturn(getNodeIterator(node));
        assertThat(hvh.getAllFrozenNodes().count(), is(1L));
    }

    @Test
    public void testGetVersion() throws RepositoryException {
        when(versionHistory.getVersion("id")).thenReturn(version);
        when(versionHistory.getVersion("some-other-id")).thenThrow(VersionException.class);
        assertThat(hvh.getVersion("id").get().get(), is(version));
        assertThat(hvh.getVersion("some-other-id").isPresent(), is(false));
    }

    @Test
    public void testGetVersionByLabel() throws RepositoryException {
        when(versionHistory.hasVersionLabel("label")).thenReturn(true, false);
        when(versionHistory.getVersionByLabel("label")).thenReturn(version);
        assertThat(hvh.getVersionByLabel("label").get().get(), is(version));
        assertThat(hvh.getVersionByLabel("label").isPresent(), is(false));
    }

    @Test
    public void testAddVersionLabel() throws RepositoryException {
        final HippoVersionHistory result = hvh.addVersionLabel("id", "X");
        assertThat(result.get(), is(hvh.get()));
        verify(versionHistory).addVersionLabel("id", "X", false);
    }

    @Test(expected = VersionException.class)
    public void testAddVersionLabel_For_Existing_Label() throws RepositoryException {
        doThrow(LabelExistsVersionException.class).when(versionHistory).addVersionLabel("id", "X", false);
        hvh.addVersionLabel("id", "X");
    }

    @Test(expected = RuntimeRepositoryException.class)
    public void testAddVersionLabel_Fails() throws RepositoryException {
        doThrow(RepositoryException.class).when(versionHistory).addVersionLabel("id", "X", false);
        hvh.addVersionLabel("id", "X");
    }

    @Test
    public void testAddOrMoveVersionLabel() throws RepositoryException {
        final HippoVersionHistory result = hvh.addOrMoveVersionLabel("id", "X");
        assertThat(result.get(), is(hvh.get()));
        verify(versionHistory).addVersionLabel("id", "X", true);
    }

    @Test(expected = VersionException.class)
    public void testAddOrMoveVersionLabel_For_Existing_Label() throws RepositoryException {
        doThrow(VersionException.class).when(versionHistory).addVersionLabel("id", "X", true);
        hvh.addOrMoveVersionLabel("id", "X");
    }

    @Test(expected = RuntimeRepositoryException.class)
    public void testAddOrMoveVersionLabel_Fails() throws RepositoryException {
        doThrow(RepositoryException.class).when(versionHistory).addVersionLabel("id", "X", true);
        hvh.addOrMoveVersionLabel("id", "X");
    }

    @Test
    public void testRemoveVersion() throws VersionException, UnsupportedRepositoryOperationException {
        hvh.removeVersion("X");
    }

    @Test
    public void testRemoveVersionLabel() throws VersionException {
        hvh.removeVersionLabel("X");
    }

    @Test
    public void testHasVersionLabel() throws RepositoryException {
        when(versionHistory.hasVersionLabel("X")).thenReturn(true);
        assertThat(hvh.hasVersionLabel("X"), is(true));
        assertThat(hvh.hasVersionLabel("Y"), is(false));
    }

    @Test
    public void testHasVersionLabel_For_Version() throws RepositoryException {
        when(versionHistory.hasVersionLabel(version, "X")).thenReturn(true);
        assertThat(hvh.hasVersionLabel(new HippoVersionImpl(version), "X"), is(true));
        assertThat(hvh.hasVersionLabel(new HippoVersionImpl(version), "Y"), is(false));
    }

    @Test
    public void testGetVersionLabels() throws RepositoryException {
        when(versionHistory.getVersionLabels()).thenReturn(new String[]{"x"});
        assertThat(hvh.getVersionLabels().collect(toList()), is(Arrays.asList("x")));
    }

    @Test
    public void testGetVersionLabels_For_Version() throws RepositoryException {
        when(versionHistory.getVersionLabels(version)).thenReturn(new String[]{"x"});
        assertThat(hvh.getVersionLabels(new HippoVersionImpl(version)).collect(toList()), is(Arrays.asList("x")));
    }
}
