/*
 *    Copyright (c) 2019 - Javen Liu (github.com/jiefenn8)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.jiefenn8.graphloom.r2rml;

import com.github.jiefenn8.graphloom.exceptions.base.GraphLoomException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import com.github.jiefenn8.graphloom.exceptions.InvalidMappingDocumentException;
import com.github.jiefenn8.graphloom.io.MappingDocument;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for R2RMLValidator code. Currently skeletal till further
 * iteration of the project or feature.
 */
@RunWith(MockitoJUnitRunner.class)
public class R2RMLValidatorTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private R2RMLValidator r2rmlValidator;

    @Before
    public void setUp() throws Exception {
        r2rmlValidator = new R2RMLValidator();
    }

    /**
     * Tests that the R2RMLValidator returns the MappingDocument when given a valid one.
     */
    @Test
    public void WhenValidateValidMappingDocument_ShouldReturnMappingDocument() {
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "/r2rml_input_01.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);

        r2rmlValidator.validate(mappingDocument);
    }

    /**
     * Tests that the R2RMLValidator throws a {@code GraphLoomException} when a given
     * {@code MappingDocument} does not exist (null).
     */
    @Test(expected = GraphLoomException.class)
    public void WhenValidateInvalidMappingDocument_ShouldThrowException() {
        MappingDocument mappingDocument = null;
        r2rmlValidator.validate(mappingDocument);
    }

    /**
     * Tests that the R2RMLValidator throws a {@code InvalidMappingDocumentException}
     * when the validator cannot find any triples maps from checking if there is
     * any logical table property (Which a TriplesMap should have).
     */
    @Test
    public void WhenValidateMissingTriplesMap_ShouldThrowException() {
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "/r2rml_no_triplesMap.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);
        exceptionRule.expect(InvalidMappingDocumentException.class);
        exceptionRule.expectMessage("No TriplesMap found.");

        r2rmlValidator.validate(mappingDocument);
    }

    /**
     * Tests that the R2RMLValidator throws a {@code InvalidMappingDocumentException}
     * when the validator cannot find any subject map.
     */
    @Test
    public void WhenValidateMissingSubjectMap_ShouldThrowException() {
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "/r2rml_no_subjectMap.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);
        exceptionRule.expect(InvalidMappingDocumentException.class);
        exceptionRule.expectMessage("SubjectMap not found.");

        r2rmlValidator.validate(mappingDocument);
    }

    /**
     * Tests that the R2RMLValidator throws a {@code InvalidMappingDocumentException}
     * when a logical table is invalid.
     */
    @Test
    public void WhenValidateInvalidLogicalTableBase_ShouldThrowException() {
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "/r2rml_invalid_logicalTable.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);
        exceptionRule.expect(InvalidMappingDocumentException.class);
        exceptionRule.expectMessage("BaseTableOrView or R2RMLView not defined.");

        r2rmlValidator.validate(mappingDocument);
    }

    /**
     * Tests that the R2RMLValidator throws a {@code InvalidMappingDocumentException}
     * when a logical table is invalid.
     */
    @Test
    public void WhenValidateInvalidLogicalTableR2RML_ShouldThrowException() {
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "/r2rml_invalid_logicalTable02.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);
        exceptionRule.expect(InvalidMappingDocumentException.class);
        exceptionRule.expectMessage("Both BaseTableOrView and R2RMLView defined.");

        r2rmlValidator.validate(mappingDocument);
    }

    /**
     * Tests that the R2RMLValidator throws a {@code InvalidMappingDocumentException}
     * when a subject map is invalid.
     */
    @Test
    public void WhenValidateInvalidSubjectMap_ShouldThrowException() {
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "/r2rml_invalid_subjectMap.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);
        exceptionRule.expect(InvalidMappingDocumentException.class);
        exceptionRule.expectMessage("Template not defined.");

        r2rmlValidator.validate(mappingDocument);
    }

    /**
     * Tests that the R2RMLValidator throws a {@code InvalidMappingDocumentException}
     * when a predicate map is invalid.
     */
    @Test
    public void WhenValidateMissingPredicateMap_ShouldThrowException() {
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "/r2rml_no_predicateMap.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);
        exceptionRule.expect(InvalidMappingDocumentException.class);
        exceptionRule.expectMessage("PredicateMap not found.");

        r2rmlValidator.validate(mappingDocument);
    }

    /**
     * Tests that the R2RMLValidator throws a {@code InvalidMappingDocumentException}
     * when a object map is invalid.
     */
    @Test
    public void WhenValidateMissingObjectMap_ShouldThrowException() {
        MappingDocument mappingDocument = mock(MappingDocument.class);
        String r2rmlFile = "/r2rml_no_objectMap.ttl";
        Model graph = ModelFactory.createDefaultModel().read(
                getClass().getResourceAsStream(r2rmlFile), null, "TTL");
        when(mappingDocument.getMappingGraph()).thenReturn(graph);
        exceptionRule.expect(InvalidMappingDocumentException.class);
        exceptionRule.expectMessage("ObjectMap not found.");

        r2rmlValidator.validate(mappingDocument);
    }

}
