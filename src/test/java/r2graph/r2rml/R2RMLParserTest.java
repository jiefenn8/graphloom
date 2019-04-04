package r2graph.r2rml;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import r2graph.configmap.ConfigMap;
import r2graph.configmap.EntityMap;
import r2graph.configmap.PredicateObjectMap;
import r2graph.configmap.impl.PredicateObjectMapCom;
import r2graph.util.MappingDocument;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.mockito.Mockito.when;

/**
 * Tests for R2RMLParser code. Currently skeletal till further
 * iteration of the project or feature.
 */
@RunWith(MockitoJUnitRunner.class)
public class R2RMLParserTest {

    private final String triplesMap = "TriplesMap1";
    private final String entitySource = "EMP";
    private final String classType = "http://example.com/ns#Employee";
    private final String template = "http://data.example.com/employee/{EMPNO}";
    private final String predicate = "http://example.com/ns#name";
    private final String column = "ENAME";
    @Mock
    MappingDocument mappingDocument;
    private R2RMLParser r2rmlParser;
    @Mock
    private TriplesMap expectedResult;

    @Before
    public void setUp() throws Exception {
        r2rmlParser = new R2RMLParser();
        setupFakeTriplesMap();

        Model r2rmlInput = ModelFactory.createDefaultModel();
        r2rmlInput.read(getClass().getResourceAsStream("../../r2rml_input_tableName_singlePom_01.ttl"), "http://example.com/ns#", "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(r2rmlInput);
    }

    private void setupFakeTriplesMap(){
        expectedResult = new TriplesMap();
        expectedResult.setEntitySource(entitySource);
        expectedResult.setClassType(classType);
        expectedResult.setTemplate(template);
        PredicateObjectMap predicateObjectMap = new PredicateObjectMapCom();
        predicateObjectMap.setPredicate(predicate);
        predicateObjectMap.setObjectSource(column);
        expectedResult.addPredicateObjectMap(predicateObjectMap);
    }

    /**
     * Tests that the R2RMLParser returns a {@code R2RMLMap} after parsing
     * a given {@code MappingDocument}.
     */
    @Test
    public void WhenParseMappingDocument_ShouldReturnR2RMLMap() {
        ConfigMap result = r2rmlParser.parse(mappingDocument);
        assertThat(result, is(notNullValue()));
    }

    /**
     * Tests that the R2RMLParser returns a populated {@code R2RMLMap} from data
     * in {@code MappingDocument}. Note: Only a shallow comparision atm.
     */
    @Test
    public void WhenParseValidMappingDocument_ShouldReturnPopulatedR2RMLMap() {
        EntityMap result = r2rmlParser.parse(mappingDocument).listEntityMaps().get(triplesMap);
        assertThat(result, samePropertyValuesAs(expectedResult));
    }
}