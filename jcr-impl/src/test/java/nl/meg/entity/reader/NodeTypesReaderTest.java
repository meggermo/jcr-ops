package nl.meg.entity.reader;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import nl.meg.AbstractMockitoTest;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class NodeTypesReaderTest extends AbstractMockitoTest {

    @Mock
    private Node node;
    @Mock
    private NodeType nt0, nt1, nt2, nt3;

    @Test
    void apply() throws RepositoryException {

        final Predicate<NodeType> filter = nt -> !nt.getName().equals("nt2");
        final Function<NodeType, List<String>> typesReader = new NodeTypesReader(filter);

        when(nt0.getSupertypes()).thenReturn(new NodeType[]{nt1, this.nt2});
        when(nt1.getSupertypes()).thenReturn(new NodeType[]{nt3});
        when(nt3.getSupertypes()).thenReturn(new NodeType[0]);

        doReturn("nt0").when(nt0).getName();
        doReturn("nt1").when(nt1).getName();
        doReturn("nt2").when(this.nt2).getName();
        doReturn("nt3").when(nt3).getName();

        Assertions.assertThat(typesReader.apply(nt0)).containsExactly("nt0", "nt1", "nt3");
    }
}
