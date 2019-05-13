package r2graph.r2rml;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import r2graph.exceptions.InvalidMappingDocumentException;
import r2graph.exceptions.base.FeijoaException;
import r2graph.io.MappingDocument;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for R2RMLValidator code. Currently skeletal till further
 * iteration of the project or feature.
 */
@RunWith(MockitoJUnitRunner.class)
public class R2RMLValidatorTest {

    private R2RMLValidator r2rmlValidator;

    @Before
    public void setUp() throws Exception {
        r2rmlValidator = new R2RMLValidator();
    }

    /**
     * Tests that the R2RMLValidator returns the MappingDocument when given a valid one.
     */
    @Test
    public void WhenValidateValidMappingDocument_ShouldReturnMappingDocument(){
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "../../r2rml_input_01.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);

        r2rmlValidator.validate(mappingDocument);
    }

    /**
     * Tests that the R2RMLValidator throws a {@code FeijoaException} when a given
     * {@code MappingDocument} does not exist (null).
     */
    @Test(expected = FeijoaException.class)
    public void WhenValidateInvalidMappingDocument_ShouldThrowException(){
        MappingDocument mappingDocument = null;
        r2rmlValidator.validate(mappingDocument);
    }

    /**
     * Tests that the R2RMLValidator throws a {@code InvalidMappingDocumentException}
     * when the validator cannot find any triples maps from checking if there is
     * any logical table property (Which a TriplesMap should have).
     */
    @Test(expected = InvalidMappingDocumentException.class)
    public void WhenValidateMissingTriplesMap_ShouldThrowException(){
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "../../r2rml_no_triplesMap.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);

        r2rmlValidator.validate(mappingDocument);
    }

    /**
     * Tests that the R2RMLValidator throws a {@code InvalidMappingDocumentException}
     * when the validator cannot find any subject map.
     */
    @Test(expected = InvalidMappingDocumentException.class)
    public void WhenValidateMissingSubjectMap_ShouldThrowException(){
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "../../r2rml_no_subjectMap.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);

        r2rmlValidator.validate(mappingDocument);
    }

    /**
     * Tests that the R2RMLValidator throws a {@code InvalidMappingDocumentException}
     * when a logical table is invalid.
     */
    @Test(expected = InvalidMappingDocumentException.class)
    public void WhenValidateInvalidLogicalTable_ShouldThrowException(){
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "../../r2rml_invalid_logicalTable.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);

        r2rmlValidator.validate(mappingDocument);
    }

    /**
     * Tests that the R2RMLValidator throws a {@code InvalidMappingDocumentException}
     * when a subject map is invalid.
     */
    @Test(expected = InvalidMappingDocumentException.class)
    public void WhenValidateInvalidSubjectMap_ShouldThrowException(){
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "../../r2rml_invalid_subjectMap.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);

        r2rmlValidator.validate(mappingDocument);
    }
}