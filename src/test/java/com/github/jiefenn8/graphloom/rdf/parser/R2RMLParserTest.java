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

package com.github.jiefenn8.graphloom.rdf.parser;

import com.github.jiefenn8.graphloom.exceptions.ParserException;
import com.github.jiefenn8.graphloom.rdf.r2rml.R2RMLMap;
import com.google.common.collect.ImmutableList;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.jena.rdf.model.*;
import org.apache.jena.shared.NotFoundException;
import org.apache.jena.util.FileManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

//todo: Refactor using fake r2rml map with mock. Also can trim unneeded mock calls.
@RunWith(JUnitParamsRunner.class)
public class R2RMLParserTest {

    private final String filename = "r2rml_file.ttl";
    private final String r2rmlNamespace = "http://www.w3c.org/ns/r2rml#";
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule public ExpectedException exceptionRule = ExpectedException.none();
    private R2RMLParser r2rmlParser;
    @Mock private FileManager mockFileManager;
    @Mock private Model mockGraph;
    @Mock private Resource mockResource;
    @Mock private Property mockProperty;
    @Mock private Statement mockStatement;

    @Before
    public void SetUp() {
        //SUT instance creation
        r2rmlParser = new R2RMLParser(mockFileManager);

        //Mock setup
        ResIterator mockResIter = mock(ResIterator.class);
        StmtIterator mockStmtIter = mock(StmtIterator.class);
        Literal mockLiteral = mock(Literal.class);

        //Default mock behaviour setup
        //All constant TermMap setup are set as shortcut.
        when(mockFileManager.loadModel(anyString(), any(), anyString())).thenReturn(mockGraph);
        when(mockGraph.getNsPrefixURI(anyString())).thenReturn(r2rmlNamespace);
        when(mockGraph.listResourcesWithProperty(any(Property.class))).thenReturn(mockResIter);
        when(mockResIter.toList()).thenReturn(Collections.singletonList(mockResource));
        when(mockResource.hasProperty(any(Property.class))).thenReturn(true);
        when(mockResource.getPropertyResourceValue(any(Property.class))).thenReturn(mockResource);
        when(mockResource.getProperty(any(Property.class))).thenReturn(mockStatement);
        when(mockResource.asResource()).thenReturn(mockResource);
        when(mockResource.listProperties(any(Property.class))).thenReturn(mockStmtIter);
        when(mockResource.as(Property.class)).thenReturn(mockProperty);
        when(mockStmtIter.toList()).thenReturn(ImmutableList.of(mockStatement));
        when(mockStatement.getLiteral()).thenReturn(mockLiteral);
        when(mockStatement.getObject()).thenReturn(mockResource);
        when(mockStatement.getPredicate()).thenReturn(mockProperty);
        when(mockLiteral.getString()).thenReturn("LITERAL");
        when(mockProperty.getLocalName()).thenReturn("PROPERTY");
    }

    @Test
    public void WhenValidFileGiven_ThenReturnR2RMLMap() {
        //Act, Assert
        R2RMLMap result = r2rmlParser.parse(filename, null);

        assertThat(result, notNullValue());
    }

    @Test
    public void WhenInvalidFilePathGiven_ThenThrowException() {
        String invalid_path = "invalid_filepath.ttl";
        when(mockFileManager.loadModel(anyString(), any(), anyString())).thenThrow(NotFoundException.class);
        exceptionRule.expect(NotFoundException.class);

        //Act, Assert
        r2rmlParser.parse(invalid_path);
    }

    @Test
    public void WhenNoNamespaceGiven_ThenThrowException() {
        String expectedMessage = "'rr' prefix uri not found.";
        when(mockGraph.getNsPrefixURI(anyString())).thenReturn(null);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expectedMessage);

        //Act, Assert
        r2rmlParser.parse(filename);
    }

    @Test
    public void WhenNoTriplesMapGiven_ThenThrowException() {
        String expected = "No valid Triples Map with rr:logicalTable found.";
        when(mockGraph.listResourcesWithProperty(any(Property.class))).thenReturn(mock(ResIterator.class));
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expected);

        //Act, Assert
        r2rmlParser.parse(filename);
    }

    @Test
    public void GivenNoBaseTableOrView_WhenParse_ThenSearchForSQLQueryProperty(){
        Property tableName = ResourceFactory.createProperty(r2rmlNamespace, "tableName");
        Property sqlQuery = ResourceFactory.createProperty(r2rmlNamespace, "sqlQuery");
        when(mockResource.hasProperty(tableName)).thenReturn(false);
        r2rmlParser.parse(filename);
        verify(mockResource, times(1)).hasProperty(sqlQuery);
    }

    @Test
    public void GivenNoR2RMLView_WhenParse_ThenThrowException(){
        Property tableName = ResourceFactory.createProperty(r2rmlNamespace, "tableName");
        Property sqlQuery = ResourceFactory.createProperty(r2rmlNamespace, "sqlQuery");
        when(mockResource.hasProperty(tableName)).thenReturn(false);
        when(mockResource.hasProperty(sqlQuery)).thenReturn(false);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage("No BaseTableOrView or R2RMLView property found.");
        r2rmlParser.parse(filename);
    }

    @Test
    public void GivenNoSqlVersion_WhenParse_ThenThrowException(){
        Property tableName = ResourceFactory.createProperty(r2rmlNamespace, "tableName");
        Property sqlVersion = ResourceFactory.createProperty(r2rmlNamespace, "sqlVersion");
        Property sqlQuery = ResourceFactory.createProperty(r2rmlNamespace, "sqlQuery");
        when(mockResource.hasProperty(tableName)).thenReturn(false);
        when(mockResource.hasProperty(sqlQuery)).thenReturn(true);
        when(mockResource.hasProperty(sqlVersion)).thenReturn(false);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage("SqlVersion property not found with SqlQuery.");
        r2rmlParser.parse(filename);
    }

    @Test
    public void WhenNonConstantPredicateMapGiven_ThenThrowException() {
        //Override default setup
        Statement mockPredicateMapStmt = mock(Statement.class);
        Property predicate = ResourceFactory.createProperty(r2rmlNamespace, "predicateMap");
        when(mockResource.getProperty(predicate)).thenReturn(mockPredicateMapStmt);

        //Setup expectations
        Resource mockPredicateMap = mock(Resource.class);
        when(mockPredicateMapStmt.getObject()).thenReturn(mockPredicateMap);
        when(mockPredicateMapStmt.getPredicate()).thenReturn(mockProperty);
        when(mockPredicateMap.asResource()).thenReturn(mockPredicateMap);
        when(mockPredicateMap.hasProperty(any(Property.class))).thenReturn(false);

        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage("Predicate Map restricted to constant only.");

        //Act, Assert
        r2rmlParser.parse(filename);
    }

    @Test
    @Parameters({"subjectMap", "objectMap"})
    public void WhenTemplateTermMapGiven_ThenReturnR2RMLMap(String termMapName) {
        //Override default setup
        Property constant = ResourceFactory.createProperty(r2rmlNamespace, "constant");
        Statement mockTermMapStmt = mock(Statement.class);
        Property predicate = ResourceFactory.createProperty(r2rmlNamespace, termMapName);
        when(mockResource.getProperty(predicate)).thenReturn(mockTermMapStmt);

        //Setup expectations
        Property template = ResourceFactory.createProperty(r2rmlNamespace, "template");
        Resource mockTermMapNode = mock(Resource.class);
        when(mockTermMapStmt.getProperty(any(Property.class))).thenReturn(mockStatement);
        when(mockTermMapStmt.getObject()).thenReturn(mockTermMapNode);
        when(mockTermMapStmt.getPredicate()).thenReturn(mockProperty);
        when(mockTermMapNode.asResource()).thenReturn(mockTermMapNode);
        when(mockTermMapNode.hasProperty(any(Property.class))).thenReturn(true);
        when(mockTermMapNode.hasProperty(constant)).thenReturn(false);
        when(mockTermMapNode.getProperty(template)).thenReturn(mockStatement);

        //Act, Assert
        r2rmlParser.parse(filename);
    }

    @Test
    public void WhenColumnObjectMapGiven_ThenReturnR2RMLMap() {
        //Override default setup
        Property constant = ResourceFactory.createProperty(r2rmlNamespace, "constant");
        Statement mockTermMapStmt = mock(Statement.class);
        Property predicate = ResourceFactory.createProperty(r2rmlNamespace, "objectMap");
        when(mockResource.getProperty(predicate)).thenReturn(mockTermMapStmt);

        //Setup expectations
        Property template = ResourceFactory.createProperty(r2rmlNamespace, "column");
        Resource mockTermMapNode = mock(Resource.class);
        when(mockTermMapStmt.getObject()).thenReturn(mockTermMapNode);
        when(mockTermMapStmt.getPredicate()).thenReturn(mockProperty);
        when(mockTermMapNode.asResource()).thenReturn(mockTermMapNode);
        when(mockTermMapNode.hasProperty(constant)).thenReturn(false);
        when(mockTermMapNode.hasProperty(template)).thenReturn(true);
        when(mockTermMapNode.getProperty(template)).thenReturn(mockStatement);

        //Act, Assert
        r2rmlParser.parse(filename);
    }

    //End
}