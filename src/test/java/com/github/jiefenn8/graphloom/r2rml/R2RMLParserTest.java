package com.github.jiefenn8.graphloom.r2rml;

import com.github.jiefenn8.graphloom.exceptions.base.GraphLoomException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.github.jiefenn8.graphloom.configmap.ConfigMap;
import com.github.jiefenn8.graphloom.configmap.EntityMap;
import com.github.jiefenn8.graphloom.io.MappingDocument;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.mockito.Mockito.when;

/**
 * Tests for R2RMLParser code. Currently skeletal till further
 * iteration of the project or feature.
 * <p>
 * All tests assumes any validation check done by {@code R2RMLValidator}
 * is not enabled unless specified by test case.
 */
@RunWith(MockitoJUnitRunner.class)
public class R2RMLParserTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private final String r2rmlFileName = "/r2rml_input_01.ttl";
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
        r2rmlParser.disableValidation(true);
        setupFakeTriplesMap();
    }

    private void setupFakeTriplesMap() {
        expectedResult = new TriplesMap();
        expectedResult.setEntitySource(entitySource);
        expectedResult.setClassType(classType);
        expectedResult.setTemplate(template);
        PredicateObjectMap predicateObjectMap = new PredicateObjectMap();
        predicateObjectMap.setPredicate(predicate);
        predicateObjectMap.setObjectSource(column);
        expectedResult.addPredicateMap(predicateObjectMap);
    }

    /**
     * Tests that the R2RMLParser returns a {@code R2RMLMap} after parsing
     * a given {@code MappingDocument}.
     */
    @Test
    public void WhenParseMappingDocument_ShouldReturnR2RMLMap() {
        Model r2rmlInput = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFileName), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(r2rmlInput);

        ConfigMap result = r2rmlParser.parse(mappingDocument);
        assertThat(result, is(notNullValue()));
    }

    /**
     * Tests that the R2RMLParser returns a populated {@code R2RMLMap} from
     * data in {@code MappingDocument}. Note: Only a shallow comparision atm.
     */
    @Test
    public void WhenParseValidMappingDocument_ShouldReturnPopulatedR2RMLMap() {
        Model r2rmlInput = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFileName), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(r2rmlInput);

        EntityMap result = r2rmlParser.parse(mappingDocument).listEntityMaps().get(triplesMap);
        assertThat(result, samePropertyValuesAs(expectedResult));
    }

    /**
     * Tests that the R2RMLParser throws an GraphLoomException when a given
     * {@code MappingDocument} does not exist (null).
     */
    @Test(expected = GraphLoomException.class)
    public void WhenParseInvalidMappingDocument_ShouldThrowException() {
        r2rmlParser.parse(null);
    }

    /**
     * Tests that the R2RML Parser throws an GraphLoomException when a given
     * {@code MappingDocument} have missing subjectMap in its triplesMap.
     */
    @Test
    public void WhenParseMissingSubjectMap_ShouldThrowException(){
        Model r2rmlInput = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream("/r2rml_no_subjectMap.ttl"), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(r2rmlInput);
        exceptionRule.expect(GraphLoomException.class);
        exceptionRule.expectMessage("SubjectMap not found.");

        r2rmlParser.parse(mappingDocument);
    }

    /**
     * Tests that the R2RML Parser throws an GraphLoomException when a given
     * {@code MappingDocument} have missing predicateMap in a predicateObjectMap.
     */
    @Test
    public void WhenParseMissingPredicateMap_ShouldThrowException(){
        Model r2rmlInput = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream("/r2rml_no_predicateMap.ttl"), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(r2rmlInput);
        exceptionRule.expect(GraphLoomException.class);
        exceptionRule.expectMessage("PredicateMap not found.");

        r2rmlParser.parse(mappingDocument);
    }

    /**
     * Tests that the R2RML Parser throws an GraphLoomException when a given
     * {@code MappingDocument} have missing subjectMap in a predicateObjectMap.
     */
    @Test
    public void WhenParseMissingObjectMap_ShouldThrowException(){
        Model r2rmlInput = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream("/r2rml_no_objectMap.ttl"), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(r2rmlInput);
        exceptionRule.expect(GraphLoomException.class);
        exceptionRule.expectMessage("ObjectMap not found.");

        r2rmlParser.parse(mappingDocument);
    }
}