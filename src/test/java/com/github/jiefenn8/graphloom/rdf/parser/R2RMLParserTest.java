/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.parser;

import com.github.jiefenn8.graphloom.exceptions.ParserException;
import com.github.jiefenn8.graphloom.rdf.r2rml.R2RMLSyntax;
import com.google.common.collect.ImmutableList;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.jena.rdf.model.*;
import org.apache.jena.shared.NotFoundException;
import org.apache.jena.util.FileManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link R2RMLParser}.
 */
@RunWith(JUnitParamsRunner.class)
public class R2RMLParserTest {

    private static final String VALID_FILENAME = "r2rml_file.ttl";
    private final Model model = ModelFactory.createDefaultModel();
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule public ExpectedException exceptionRule = ExpectedException.none();
    private R2RMLParser r2rmlParser;
    @Mock private FileManager mockFileManager;

    @Before
    public void setup() {
        when(mockFileManager.loadModel(anyString(), any(), anyString()))
                .thenReturn(model);

        r2rmlParser = new R2RMLParser(mockFileManager);
        r2rmlParser.parse(VALID_FILENAME, null);
    }

    @Test
    public void GivenNullFileManager_WhenCreateCtor_ThenThrowException() {
        exceptionRule.expect(NullPointerException.class);

        r2rmlParser = new R2RMLParser(null);
    }

    @Test
    public void GivenNullFile_WhenParse_ThenThrowException() {
        exceptionRule.expect(NotFoundException.class);
        when(mockFileManager.loadModel(any(), any(), anyString()))
                .thenThrow(NotFoundException.class);

        r2rmlParser = new R2RMLParser(mockFileManager);
        r2rmlParser.parse(null, null);
    }

    @Test
    public void GivenValidFile_WhenParse_ThenReturnTrue() {
        r2rmlParser = new R2RMLParser(mockFileManager);
        boolean result = r2rmlParser.parse(VALID_FILENAME, null);
        assertThat(result, is(true));
    }

    @Test
    public void GivenInvalidFile_WhenParse_ThenThrowException() {
        exceptionRule.expect(NotFoundException.class);
        when(mockFileManager.loadModel(anyString(), any(), anyString())).thenThrow(NotFoundException.class);

        String value = "invalid_file.ttl";
        r2rmlParser = new R2RMLParser(mockFileManager);
        r2rmlParser.parse(value, null);
    }

    @Test
    public void GivenParserHaveOpenedFile_WhenParse_ThenReturnFalse() {
        boolean result = r2rmlParser.parse(VALID_FILENAME, null);
        assertThat(result, is(false));
    }

    @Test
    public void GivenFile_WhenClose_ThenVerifyClose() {
        boolean result = r2rmlParser.close();
        assertThat(result, is(true));
    }

    @Test
    public void GivenNoNsPrefix_WhenGetNsPrefixMap_ThenReturnEmptyMap() {
        Map<String, String> result = r2rmlParser.getNsPrefixMap();
        assertThat(result, is(anEmptyMap()));
    }

    @Test
    public void GivenNoTriplesMap_WhenGetTriplesMap_ThenReturnEmptyList() {
        Set<Resource> result = r2rmlParser.getTriplesMaps();
        assertThat(result, is(empty()));
    }

    @Test
    public void GivenTriplesMap_WhenGetTriplesMap_ThenReturnList() {
        model.createResource("#TriplesMap1")
                .addProperty(R2RMLSyntax.logicalTable, model.createResource())
                .addProperty(R2RMLSyntax.subjectMap, model.createResource());
        Set<Resource> result = r2rmlParser.getTriplesMaps();
        assertThat(result, is(not(empty())));
    }

    @Test
    public void GivenResourceWithNoLogicalTable_WhenGetLogicalTable_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property property = R2RMLSyntax.logicalTable;
        String expected = String.format("%s property not found in %s.", property, value);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expected);

        r2rmlParser.getLogicalTable(value);
    }

    @Test
    public void GivenResourceWithNoR2RMLView_WhenCheckIsR2RMLView_ThenReturnFalse() {
        Resource value = model.createResource("RESOURCE");
        boolean result = r2rmlParser.isR2RMLView(value);
        assertThat(result, is(false));
    }

    @Test
    public void GivenResourceWithR2RMLViewReference_WhenCheckIsBaseTableOrView_ThenReturnTrue() {
        Resource value = model.createResource("#REFERENCE")
                .addProperty(R2RMLSyntax.sqlQuery, "QUERY")
                .addProperty(R2RMLSyntax.sqlVersion, "VERSION");
        boolean result = r2rmlParser.isR2RMLView(value);
        assertThat(result, is(true));

    }

    @Test
    public void GivenResourceWithNoBaseTableOrView_WhenGetTableName_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        String expected = String.format("%s property not found in %s.", R2RMLSyntax.tableName, value);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expected);

        r2rmlParser.getTableName(value);
    }

    @Test
    public void GivenResourceWithNoR2RMLView_WhenGetSqlQuery_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        String expected = String.format("%s property not found in %s.", R2RMLSyntax.sqlQuery, value);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expected);

        r2rmlParser.getSqlQuery(value);
    }

    @Test
    public void GivenResourceWithNoR2RMLView_WhenGetSqlVersion_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        String expected = String.format("%s property not found in %s.", R2RMLSyntax.sqlVersion, value);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expected);

        r2rmlParser.getVersion(value);
    }

    @Test
    public void GivenResourceWithNoBaseTableOrView_WhenCheckIsBaseTableOrView_ThenReturnFalse() {
        Resource value = model.createResource("RESOURCE");
        boolean result = r2rmlParser.isBaseTableOrView(value);
        assertThat(result, is(false));
    }

    @Test
    public void GivenResourceWithBaseTableOrViewReference_WhenCheckIsBaseTableOrView_ThenReturnTrue() {
        Resource value = model.createResource("#REFERENCE")
                .addProperty(R2RMLSyntax.tableName, "TABLE_NAME");
        boolean result = r2rmlParser.isBaseTableOrView(value);
        assertThat(result, is(true));
    }

    @Test
    public void GivenResourceWithNoSubjectMap_WhenGetSubjectMap_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property termMap = R2RMLSyntax.subjectMap;
        Property constTermMap = R2RMLSyntax.subject;
        String expected = String.format("%s or %s term map not found in %s.", termMap, constTermMap, value);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expected);

        r2rmlParser.getSubjectMap(value);
    }

    public List<Property> subjectMapParameters() {
        return ImmutableList.of(R2RMLSyntax.subject, R2RMLSyntax.subjectMap);
    }

    @Test
    @Parameters(method = "subjectMapParameters")
    public void GivenResourceWithSubjectMap_WhenGetSubjectMap_ThenReturnResource(Property subjectMap) {
        Resource value = model.createResource().addProperty(subjectMap, model.createResource());

        Statement result = r2rmlParser.getSubjectMap(value);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenResourceWithNoEntityClasses_WhenGetEntityClasses_ThenReturnEmptyList() {
        Resource value = model.createResource("RESOURCE");
        Set<Resource> result = r2rmlParser.listEntityClasses(value);
        assertThat(result, is(empty()));
    }

    @Test
    public void GivenResourceWithNoPredicateObjectMap_WhenGetPredicateObjectMap_ThenReturnEmptyList() {
        Resource value = model.createResource("RESOURCE");
        Set<Resource> result = r2rmlParser.listEntityClasses(value);
        assertThat(result, is(empty()));
    }

    @Test
    public void GivenResourceWithPredicateObjectMap_WhenGetPredicateObjectMap_ThenReturnList() {
        Resource triplesMap = model.createResource("#TriplesMap1")
                .addProperty(R2RMLSyntax.predicateObjectMap, model.createResource());
        Set<Statement> result = r2rmlParser.listPredicateObjectMaps(triplesMap);
        assertThat(result, is(not(empty())));
    }

    @Test
    public void GivenResourceWithNoPredicateMap_WhenGetPredicateMap_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property termMap = R2RMLSyntax.predicateMap;
        Property constTermMap = R2RMLSyntax.predicate;
        String expected = String.format("%s or %s term map not found in %s.", termMap, constTermMap, value);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expected);

        r2rmlParser.getPredicateMap(value);
    }

    public List<Property> predicateMapParameters() {
        return ImmutableList.of(R2RMLSyntax.predicate, R2RMLSyntax.predicateMap);
    }

    @Test
    @Parameters(method = "predicateMapParameters")
    public void GivenResourceWithPredicateMap_WhenGetPredicateMap_ThenReturnResource(Property predicateMap) {
        Resource value = model.createResource().addProperty(predicateMap, model.createResource());

        Statement result = r2rmlParser.getPredicateMap(value);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenResourceWithNoObjectMap_WhenGetObjectMap_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property termMap = R2RMLSyntax.objectMap;
        Property constTermMap = R2RMLSyntax.object;
        String expected = String.format("%s or %s term map not found in %s", termMap, constTermMap, value);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expected);

        r2rmlParser.getObjectMap(value);
    }

    public List<Property> objectMapParameters() {
        return ImmutableList.of(R2RMLSyntax.object, R2RMLSyntax.objectMap);
    }

    @Test
    @Parameters(method = "objectMapParameters")
    public void GivenResourceWithObjectMap_WhenGetObjectMap_ThenReturnResource(Property objectMap) {
        Resource value = model.createResource().addProperty(objectMap, model.createResource());

        Statement result = r2rmlParser.getObjectMap(value);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenStatementIsNotConstant_WhenCheckIsConstant_ThenReturnFalse() {
        Resource resource = model.createResource("RESOURCE");
        Property property = model.createProperty("PROPERTY");
        RDFNode node = model.createResource();
        Statement value = model.createStatement(resource, property, node);

        boolean result = r2rmlParser.isConstant(value);
        assertThat(result, is(false));
    }

    @Test
    public void GivenStatementWithNoConstant_WhenGetConstantValue_ThenThrowException() {
        Resource resource = model.createResource("RESOURCE");
        Property property = model.createProperty("PROPERTY");
        RDFNode node = model.createResource();
        Statement value = model.createStatement(resource, property, node);
        Property termMap = R2RMLSyntax.constant;
        String expected = String.format("%s property not found in %s.", termMap, node);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expected);

        r2rmlParser.getConstantValue(value);
    }

    public List<Property> constTermMapShortcutParameters() {
        return ImmutableList.of(R2RMLSyntax.subject, R2RMLSyntax.predicate, R2RMLSyntax.object);
    }

    public List<Property> constTermMapParameters() {
        return ImmutableList.of(R2RMLSyntax.subjectMap, R2RMLSyntax.predicateMap, R2RMLSyntax.objectMap);
    }

    @Test
    @Parameters(method = "constTermMapShortcutParameters")
    public void GivenStatementWithShortcut_WhenGetConstantValue_ThenReturnRDFNode(Property constTermMap) {
        Statement termMap = model.createStatement(model.createResource(), constTermMap, model.createResource());

        RDFNode result = r2rmlParser.getConstantValue(termMap);
        assertThat(result, is(notNullValue()));
    }

    @Test
    @Parameters(method = "constTermMapParameters")
    public void GivenStatementWithConstant_WhenGetConstantValue_ThenReturnRDFNode(Property constTermMap) {
        Statement termMap = model.createStatement(model.createResource(), constTermMap, model.createResource());
        termMap.getResource().addProperty(R2RMLSyntax.constant, model.createResource());

        RDFNode result = r2rmlParser.getConstantValue(termMap);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenResourceIsNotTemplate_WhenCheckIsTemplate_ThenReturnFalse() {
        Resource value = model.createResource("RESOURCE");
        boolean result = r2rmlParser.isTemplate(value);
        assertThat(result, is(false));
    }

    @Test
    public void GivenResourceWithNoTemplate_WhenGetTemplateValue_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property property = R2RMLSyntax.template;
        String expected = String.format("%s property not found in %s.", property, value);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expected);

        r2rmlParser.getTemplateValue(value);
    }

    @Test
    public void GivenResourceIsNotColumn_WhenCheckIsColumn_ThenReturnFalse() {
        Resource value = model.createResource("RESOURCE");

        boolean result = r2rmlParser.isColumn(value);
        assertThat(result, is(false));
    }

    @Test
    public void GivenResourceWithNoColumn_WhenGetColumnName_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property property = R2RMLSyntax.column;
        String expected = String.format("%s property not found in %s.", property, value);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expected);

        r2rmlParser.getColumnName(value);
    }
}