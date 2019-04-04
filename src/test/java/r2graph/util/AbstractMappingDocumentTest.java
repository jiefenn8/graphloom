package r2graph.util;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for MappingDocument code. Currently skeletal till further
 * iteration of the project or feature.
 */
public abstract class AbstractMappingDocumentTest {

    private MappingDocument mappingDocument;

    public abstract MappingDocument createInstance();

    @Before
    public void setUp() throws Exception {
        mappingDocument = createInstance();
    }

    /**
     * Tests that the MappingDocument returns a populated {@code Model} containing
     * the configuration mapping of the config file. Note: Only compares the structure
     * currently.
     */
    @Test
    public void WhenMappingDocumentExists_ShouldReturnGraph() {
        InputStream in = getClass().getResourceAsStream("../../r2rml_input_tableName_singlePom_01.ttl");
        Model expected = ModelFactory.createDefaultModel().read(in, "http://example.org/ns#", "TTL");

        Model result = mappingDocument.getMappingGraph();

        assertThat(result.isIsomorphicWith(expected), is(true));
    }
}
