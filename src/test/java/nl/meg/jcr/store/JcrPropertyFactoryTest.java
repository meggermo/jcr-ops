package nl.meg.jcr.store;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JcrPropertyFactoryTest {

    @Mock
    private Node nodeMock;
    @Mock
    private Property propertyMock;
    @Mock
    private Value valueMock;
    @Mock
    private Session sessionMock;
    @Mock
    private ValueFactory valueFactoryMock;

    @Test
    void testGetNameAndToString() {
        final JcrProperty<Boolean> p = JcrPropertyFactory.ofBoolean("test");
        assertThat(p.name()).isEqualTo("test");
        assertThat(p).hasToString("JcrProperty[name=test]");
    }

    @Test
    void testBoolean() throws RepositoryException {

        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getBoolean()).thenReturn(false, true);

        final JcrProperty<Boolean> p = JcrPropertyFactory.ofBoolean("test");
        assertThat(p.getValue(nodeMock)).isFalse();
        assertThat(p.getValue(nodeMock)).isTrue();

        p.setValue(nodeMock, true);
        verify(nodeMock).setProperty("test", true);
    }

    @Test
    void testBooleanList() throws RepositoryException {

        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getValues()).thenReturn(new Value[]{valueMock}, new Value[]{valueMock, valueMock});
        when(valueMock.getBoolean()).thenReturn(true, false, true);

        final JcrProperty<List<Boolean>> p = JcrPropertyFactory.ofBooleanList("test");
        assertThat(p.getValue(nodeMock)).isEqualTo(List.of(true));
        assertThat(p.getValue(nodeMock)).isEqualTo(List.of(false, true));

        when(nodeMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getValueFactory()).thenReturn(valueFactoryMock);
        doReturn(valueMock).when(valueFactoryMock).createValue(true);
        doReturn(valueMock).when(valueFactoryMock).createValue(false);

        p.setValue(nodeMock, List.of(true, false));
    }

    @Test
    void testBooleanOption() throws RepositoryException {

        when(nodeMock.hasProperty("test")).thenReturn(false, true);
        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getBoolean()).thenReturn(false, true);

        final JcrProperty<Optional<Boolean>> p = JcrPropertyFactory.ofBooleanOption("test");
        assertThat(p.getValue(nodeMock)).isNotPresent();
        assertThat(p.getValue(nodeMock).orElse(true)).isFalse();
        assertThat(p.getValue(nodeMock).orElse(false)).isTrue();

        p.setValue(nodeMock, Optional.empty());
        verify(propertyMock).remove();
    }

    @Test
    void testOptionPresent() throws RepositoryException {

        when(nodeMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getValueFactory()).thenReturn(valueFactoryMock);
        when(valueFactoryMock.createValue(true)).thenReturn(valueMock);

        final JcrProperty<Optional<Boolean>> p = JcrPropertyFactory.ofBooleanOption("test");
        p.setValue(nodeMock, Optional.of(true));
        verify(nodeMock).setProperty("test", valueMock);
    }

    @Test
    void testBooleanListOption() throws RepositoryException {

        when(nodeMock.hasProperty("test")).thenReturn(false, true);
        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getValues()).thenReturn(new Value[]{valueMock}, new Value[]{valueMock, valueMock});
        when(valueMock.getBoolean()).thenReturn(true, false, true);

        final JcrProperty<Optional<List<Boolean>>> p = JcrPropertyFactory.ofBooleanListOption("test");
        assertThat(p.getValue(nodeMock)).isNotPresent();
        assertThat(p.getValue(nodeMock).orElse(null)).isEqualTo(List.of(true));
        assertThat(p.getValue(nodeMock).orElse(null)).isEqualTo(List.of(false, true));

        p.setValue(nodeMock, Optional.empty());
        verify(propertyMock).remove();
    }

    @Test
    void testLong() throws RepositoryException {

        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getLong()).thenReturn(0L, 1L);

        final JcrProperty<Long> p = JcrPropertyFactory.ofLong("test");
        assertThat(p.getValue(nodeMock)).isZero();
        assertThat(p.getValue(nodeMock)).isEqualTo(1L);

        p.setValue(nodeMock, 10L);
        verify(nodeMock).setProperty("test", 10L);
    }

    @Test
    void testLongList() throws RepositoryException {

        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getValues()).thenReturn(new Value[]{valueMock}, new Value[]{valueMock, valueMock});
        when(valueMock.getLong()).thenReturn(0L, 1L, 2L);

        final JcrProperty<List<Long>> p = JcrPropertyFactory.ofLongList("test");
        assertThat(p.getValue(nodeMock)).isEqualTo(List.of(0L));
        assertThat(p.getValue(nodeMock)).isEqualTo(List.of(1L, 2L));

        when(nodeMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getValueFactory()).thenReturn(valueFactoryMock);

        doReturn(valueMock).when(valueFactoryMock).createValue(10L);
        doReturn(valueMock).when(valueFactoryMock).createValue(20L);
        p.setValue(nodeMock, List.of(10L, 20L));

        verify(nodeMock).setProperty(Mockito.eq("test"), Mockito.<Value[]>any());
    }

    @Test
    void testLongOption() throws RepositoryException {

        when(nodeMock.hasProperty("test")).thenReturn(false, true);
        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getLong()).thenReturn(0L, 1L);

        final JcrProperty<Optional<Long>> p = JcrPropertyFactory.ofLongOption("test");
        assertThat(p.getValue(nodeMock)).isNotPresent();
        assertThat(p.getValue(nodeMock).orElse(null)).isZero();
        assertThat(p.getValue(nodeMock).orElse(null)).isEqualTo(1L);
    }

    @Test
    void testLongListOption() throws RepositoryException {

        when(nodeMock.hasProperty("test")).thenReturn(false, true);
        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getValues()).thenReturn(new Value[]{valueMock}, new Value[]{valueMock, valueMock});
        when(valueMock.getLong()).thenReturn(0L, 1L, 2L);

        final JcrProperty<Optional<List<Long>>> p = JcrPropertyFactory.ofLongListOption("test");
        assertThat(p.getValue(nodeMock)).isNotPresent();
        assertThat(p.getValue(nodeMock).orElse(null)).isEqualTo(List.of(0L));
        assertThat(p.getValue(nodeMock).orElse(null)).isEqualTo(List.of(1L, 2L));
    }

    @Test
    void testString() throws RepositoryException {

        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getString()).thenReturn("x", "y");

        final JcrProperty<String> p = JcrPropertyFactory.ofString("test");
        assertThat(p.getValue(nodeMock)).isEqualTo("x");
        assertThat(p.getValue(nodeMock)).isEqualTo("y");

        p.setValue(nodeMock, "value");
        verify(nodeMock).setProperty("test", "value");
    }

    @Test
    void testStringList() throws RepositoryException {

        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getValues()).thenReturn(new Value[]{valueMock}, new Value[]{valueMock, valueMock});
        when(valueMock.getString()).thenReturn("x", "y", "z");

        final JcrProperty<List<String>> p = JcrPropertyFactory.ofStringList("test");
        assertThat(p.getValue(nodeMock)).isEqualTo(List.of("x"));
        assertThat(p.getValue(nodeMock)).isEqualTo(List.of("y", "z"));

        when(nodeMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getValueFactory()).thenReturn(valueFactoryMock);
        doReturn(valueMock).when(valueFactoryMock).createValue("x");
        doReturn(valueMock).when(valueFactoryMock).createValue("y");

        p.setValue(nodeMock, List.of("x", "y"));

        verify(nodeMock).setProperty(Mockito.eq("test"), Mockito.<Value[]>any());
    }

    @Test
    void testStringOption() throws RepositoryException {

        when(nodeMock.hasProperty("test")).thenReturn(false, true);
        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getString()).thenReturn("x", "y");

        final JcrProperty<Optional<String>> p = JcrPropertyFactory.ofStringOption("test");
        assertThat(p.getValue(nodeMock)).isNotPresent();
        assertThat(p.getValue(nodeMock).orElse(null)).isEqualTo("x");
        assertThat(p.getValue(nodeMock).orElse(null)).isEqualTo("y");
    }

    @Test
    void testStringListOption() throws RepositoryException {

        when(nodeMock.hasProperty("test")).thenReturn(false, true);
        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getValues()).thenReturn(new Value[]{valueMock}, new Value[]{valueMock, valueMock});
        when(valueMock.getString()).thenReturn("x", "y", "z");

        final JcrProperty<Optional<List<String>>> p = JcrPropertyFactory.ofStringListOption("test");
        assertThat(p.getValue(nodeMock)).isNotPresent();
        assertThat(p.getValue(nodeMock).orElse(null)).isEqualTo(List.of("x"));
        assertThat(p.getValue(nodeMock).orElse(null)).isEqualTo(List.of("y", "z"));

        when(nodeMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getValueFactory()).thenReturn(valueFactoryMock);
        doReturn(valueMock).when(valueFactoryMock).createValue("x");
        doReturn(valueMock).when(valueFactoryMock).createValue("y");
        p.setValue(nodeMock, Optional.of(List.of("x", "y")));

        verify(nodeMock).setProperty(Mockito.eq("test"), Mockito.<Value[]>any());
    }

    @Test
    void testInstant() throws RepositoryException {

        final Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(0);
        final Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(1000);
        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getDate()).thenReturn(c1, c2);

        final JcrProperty<Instant> p = JcrPropertyFactory.ofInstant("test");
        assertThat(p.getValue(nodeMock)).isEqualTo(c1.toInstant());
        assertThat(p.getValue(nodeMock)).isEqualTo(c2.toInstant());

        p.setValue(nodeMock, Instant.EPOCH);
    }

    @Test
    void testInstantList() throws RepositoryException {

        final Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(0);
        final Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(1000);
        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getValues()).thenReturn(new Value[]{valueMock}, new Value[]{valueMock, valueMock});
        when(valueMock.getDate()).thenReturn(c1, c2, c1);

        final JcrProperty<List<Instant>> p = JcrPropertyFactory.ofInstantList("test");
        assertThat(p.getValue(nodeMock)).isEqualTo(List.of(c1.toInstant()));
        assertThat(p.getValue(nodeMock)).isEqualTo(List.of(c2.toInstant(), c1.toInstant()));

        when(nodeMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getValueFactory()).thenReturn(valueFactoryMock);
        doReturn(valueMock).when(valueFactoryMock).createValue(c1);
        doReturn(valueMock).when(valueFactoryMock).createValue(c2);
        p.setValue(nodeMock, List.of(c1.toInstant(), c2.toInstant()));

        verify(nodeMock).setProperty(Mockito.eq("test"), Mockito.<Value[]>any());
    }

    @Test
    void testInstantOption() throws RepositoryException {

        final Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(0);
        final Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(1000);
        when(nodeMock.hasProperty("test")).thenReturn(false, true);
        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getDate()).thenReturn(c1, c2);

        final JcrProperty<Optional<Instant>> p = JcrPropertyFactory.ofInstantOption("test");
        assertThat(p.getValue(nodeMock)).isNotPresent();
        assertThat(p.getValue(nodeMock).orElse(null)).isEqualTo(c1.toInstant());
        assertThat(p.getValue(nodeMock).orElse(null)).isEqualTo(c2.toInstant());
    }

    @Test
    void testInstantListOption() throws RepositoryException {

        final Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(0);
        final Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(1000);
        when(nodeMock.hasProperty("test")).thenReturn(false, true);
        when(nodeMock.getProperty("test")).thenReturn(propertyMock);
        when(propertyMock.getValues()).thenReturn(new Value[]{valueMock}, new Value[]{valueMock, valueMock});
        when(valueMock.getDate()).thenReturn(c1, c2, c1);

        final JcrProperty<Optional<List<Instant>>> p = JcrPropertyFactory.ofInstantListOption("test");
        assertThat(p.getValue(nodeMock)).isNotPresent();
        assertThat(p.getValue(nodeMock).orElse(null)).isEqualTo(List.of(c1.toInstant()));
        assertThat(p.getValue(nodeMock).orElse(null)).isEqualTo(List.of(c2.toInstant(), c1.toInstant()));
    }

}


