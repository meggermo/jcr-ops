package nl.meg.entity.reader;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import nl.meg.AbstractOakIntegrationTest;
import nl.meg.jcr.entity.NodeEntity;
import nl.meg.jcr.entity.PropertyEntity;
import nl.meg.jcr.function.JcrFunction;

class NodeReaderTest extends AbstractOakIntegrationTest {


    private final Predicate<String> prefixFilter = new JcrPrefixFilter(
            Stream.concat(
                    JcrPrefixFilter.getJcrNamespacePrefixes().stream(),
                    Stream.of("oak", "rep")
            ).collect(Collectors.toSet())
    );

    @BeforeEach
    public void addCnd() {
        final String cnd = "" +
                "<'hippo'='http://www.onehippo.org/jcr/hippo/nt/2.0.4'>\n" +
                "<'nt'='http://www.jcp.org/jcr/nt/1.0'>\n" +
                "<'jcr'='http://www.jcp.org/jcr/1.0'>\n" +
                "<'mix'='http://www.jcp.org/jcr/mix/1.0'>\n" +
                "[hippo:basedocument] > nt:unstructured, mix:referenceable\n" +
                "[hippo:blogdocument] > hippo:basedocument\n";
        loadCnd(cnd);
    }

    @Test
    void apply() throws RepositoryException, JsonProcessingException {

        final Session session = getAdminSession();
        final Node test = session.getRootNode().addNode("test", "hippo:blogdocument");
        test.setProperty("x", new String[]{"y", "Z"}, PropertyType.PATH);
        test.setProperty("y", 1);
        test.setProperty("z", true);
        test.setProperty("a", BigDecimal.TEN);
        test.setProperty("b", 1.0);
        test.setProperty("c", Calendar.getInstance());
        session.save();


        final NodeReader nodeReader = new NodeReader(createTypesReader(), createPropertiesReader());
        nodeReader.setNodesReader(createNodesReader(nodeReader));

        final NodeEntity nodeEntity = nodeReader.apply(getAdminSession().getRootNode());
        final ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JaxbAnnotationModule())
                .registerModule(new JavaTimeModule())
                .configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, true)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        getLogger().info("{}", objectMapper.writeValueAsString(nodeEntity));
    }

    private JcrFunction<Node, List<NodeEntity>> createNodesReader(final NodeReader nodeReader) {
        final Predicate<Node> nodeFilter = node -> {
            try {
                return prefixFilter.test(node.getName());
            } catch (RepositoryException e) {
                return false;
            }
        };
        return new NodesReader(nodeFilter, nodeReader);
    }

    private JcrFunction<Node, List<PropertyEntity<?>>> createPropertiesReader() {
        final Predicate<Property> propertyFilter = property -> {
            try {
                return prefixFilter.test(property.getName());
            } catch (RepositoryException e) {
                return false;
            }
        };
        final Function<Property, UnaryOperator<String>> postProcessor = p -> UnaryOperator.identity();
        final JcrFunction<Property, PropertyEntity<?>> propertyReader = new PropertyReader(postProcessor);
        return new PropertiesReader(propertyFilter, propertyReader);
    }

    private Function<NodeType, List<String>> createTypesReader() {
        final Predicate<NodeType> typesFilter = nodeType -> prefixFilter.test(nodeType.getName());
        return new NodeTypesReader(typesFilter);
    }
}
