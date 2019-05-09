package r2graph.r2rml;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import r2graph.exceptions.RuleClassNotFoundException;
import r2graph.exceptions.base.FeijoaValidatorException;
import r2graph.io.MappingDocument;

import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for R2RMLValidator code. Currently skeletal till further
 * iteration of the project or feature.
 */
@RunWith(MockitoJUnitRunner.class)
public class R2RMLValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
        String r2rmlFile = "../../r2rml_input_tableName_singlePom_01.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);
    }

    /**
     * Tests that the R2RMLValidator throws a FeijoaValidatorException when a given
     * {@code MappingDocument} does not exist (null).
     */
    @Test(expected = FeijoaValidatorException.class)
    public void WhenValidateInvalidMappingDocument_ShouldThrowException(){
        MappingDocument mappingDocument = null;
        r2rmlValidator.validate(mappingDocument);
    }

    @Test(expected = RuleClassNotFoundException.class)
    public void WhenValidateInvalidTriplesMap_ShouldThrowException(){
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "../../r2rml_no_triplesMap.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);

        final String error = "No TriplesMap found.";
        //thrown.expect(isA(RuleClassNotFoundException.class));
        //thrown.expectMessage(error);
        r2rmlValidator.validate(mappingDocument);
    }
}
