/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.exceptions.ParserException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.shared.NotFoundException;
import org.apache.jena.util.FileManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.empty;
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
    private R2RMLParser r2rmlParser;
    @Mock private FileManager mockFileManager;

    @Before
    public void setUp() {
        when(mockFileManager.loadModel(anyString(), any(), anyString())).thenReturn(model);

        r2rmlParser = new R2RMLParser(mockFileManager);
        r2rmlParser.parse(VALID_FILENAME, null);
    }

    @Test
    public void GivenNullFileManager_WhenCreateInstance_ThenThrowException() {
        Assert.assertThrows(
                NullPointerException.class,
                () -> new R2RMLParser(null)
        );
    }

    public String[] parseMethodInvalidFileParameters() {
        return new String[]{null, "invalid_file.ttl", StringUtils.EMPTY};
    }

    @Test
    @Parameters(method = "parseMethodInvalidFileParameters")
    public void GivenInvalidFile_WhenParse_ThenThrowException(String file) {
        FileManager mockManagerWithException = mock(FileManager.class);
        when(mockManagerWithException.loadModel(any(), any(), anyString())).thenThrow(NotFoundException.class);

        Assert.assertThrows(
                NotFoundException.class,
                () -> {
                    r2rmlParser = new R2RMLParser(mockManagerWithException);
                    r2rmlParser.parse(file, StringUtils.EMPTY);
                }
        );
    }

    @Test
    public void GivenValidFile_WhenParse_ThenReturnTrue() {
        r2rmlParser = new R2RMLParser(mockFileManager);
        boolean result = r2rmlParser.parse(VALID_FILENAME, null);
        assertThat(result, is(true));
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
        Property firstProperty = R2RMLSyntax.logicalTable;
        Property secondProperty = R2RMLSyntax.subject;
        RDFNode blank = model.createResource();
        model.createResource("#TriplesMap1")
                .addProperty(firstProperty, blank)
                .addProperty(secondProperty, blank);

        Set<Resource> result = r2rmlParser.getTriplesMaps();
        assertThat(result, is(not(empty())));
    }

    @Test
    public void GivenResourceWithNoLogicalTable_WhenGetLogicalTable_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property property = R2RMLSyntax.logicalTable;
        String expected = property + " property not found in " + value + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlParser.getLogicalTable(value)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenResourceWithNoR2RMLView_WhenCheckIsR2RMLView_ThenReturnFalse() {
        Resource value = model.createResource("RESOURCE");
        boolean result = r2rmlParser.isR2RMLView(value);
        assertThat(result, is(false));
    }

    @Test
    public void GivenResourceWithR2RMLViewReference_WhenCheckIsBaseTableOrView_ThenReturnTrue() {
        Resource value = model.createResource("REFERENCE")
                .addProperty(R2RMLSyntax.sqlQuery, "QUERY")
                .addProperty(R2RMLSyntax.sqlVersion, "VERSION");

        boolean result = r2rmlParser.isR2RMLView(value);
        assertThat(result, is(true));
    }

    @Test
    public void GivenResourceWithNoBaseTableOrView_WhenGetTableName_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property property = R2RMLSyntax.tableName;
        String expected = property + " property not found in " + value + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlParser.getTableName(value)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenResourceWithNoR2RMLView_WhenGetSqlQuery_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property property = R2RMLSyntax.sqlQuery;
        String expected = property + " property not found in " + value + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlParser.getSqlQuery(value)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenResourceWithNoR2RMLView_WhenGetSqlVersion_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property property = R2RMLSyntax.sqlVersion;
        String expected = property + " property not found in " + value + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlParser.getVersion(value)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenResourceWithNoBaseTableOrView_WhenCheckIsBaseTableOrView_ThenReturnFalse() {
        Resource value = model.createResource("RESOURCE");
        boolean result = r2rmlParser.isBaseTableOrView(value);
        assertThat(result, is(false));
    }

    @Test
    public void GivenResourceWithBaseTableOrViewReference_WhenCheckIsBaseTableOrView_ThenReturnTrue() {
        Resource value = model.createResource("#REFERENCE");
        value.addProperty(R2RMLSyntax.tableName, "TABLE_NAME");

        boolean result = r2rmlParser.isBaseTableOrView(value);
        assertThat(result, is(true));
    }

    @Test
    public void GivenResourceWithNoSubjectMap_WhenGetSubjectMap_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property termMap = R2RMLSyntax.subjectMap;
        Property constTermMap = R2RMLSyntax.subject;
        String expected = termMap + " or " + constTermMap + " term map not found in " + value + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlParser.getSubjectMap(value)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    public List<Property> subjectMapParameters() {
        return List.of(R2RMLSyntax.subject, R2RMLSyntax.subjectMap);
    }

    @Test
    @Parameters(method = "subjectMapParameters")
    public void GivenResourceWithSubjectMap_WhenGetSubjectMap_ThenReturnResource(Property subjectMap) {
        RDFNode blank = model.createResource();
        Resource value = model.createResource().addProperty(subjectMap, blank);

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
        Property property = R2RMLSyntax.predicateObjectMap;
        RDFNode blank = model.createResource();
        Resource triplesMap = model.createResource("#TriplesMap1");
        triplesMap.addProperty(property, blank);

        Set<Statement> result = r2rmlParser.listPredicateObjectMaps(triplesMap);
        assertThat(result, is(not(empty())));
    }

    @Test
    public void GivenResourceWithNoPredicateMap_WhenGetPredicateMap_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property termMap = R2RMLSyntax.predicateMap;
        Property constTermMap = R2RMLSyntax.predicate;
        String expected = termMap + " or " + constTermMap + " term map not found in " + value + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlParser.getPredicateMap(value)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    public List<Property> predicateMapParameters() {
        return List.of(R2RMLSyntax.predicate, R2RMLSyntax.predicateMap);
    }

    @Test
    @Parameters(method = "predicateMapParameters")
    public void GivenResourceWithPredicateMap_WhenGetPredicateMap_ThenReturnResource(Property predicateMap) {
        RDFNode blank = model.createResource();
        Resource value = model.createResource().addProperty(predicateMap, blank);

        Statement result = r2rmlParser.getPredicateMap(value);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenResourceWithNoObjectMap_WhenGetObjectMap_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property termMap = R2RMLSyntax.objectMap;
        Property constTermMap = R2RMLSyntax.object;
        String expected = termMap + " or " + constTermMap + " term map not found in " + value + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlParser.getObjectMap(value)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    public List<Property> objectMapParameters() {
        return List.of(R2RMLSyntax.object, R2RMLSyntax.objectMap);
    }

    @Test
    @Parameters(method = "objectMapParameters")
    public void GivenResourceWithObjectMap_WhenGetObjectMap_ThenReturnResource(Property objectMap) {
        RDFNode blank = model.createResource();
        Resource value = model.createResource().addProperty(objectMap, blank);

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
        String expected = termMap + " property not found in " + node + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlParser.getConstantValue(value)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    public List<Property> constTermMapShortcutParameters() {
        return List.of(R2RMLSyntax.subject, R2RMLSyntax.predicate, R2RMLSyntax.object);
    }

    public List<Property> constTermMapParameters() {
        return List.of(R2RMLSyntax.subjectMap, R2RMLSyntax.predicateMap, R2RMLSyntax.objectMap);
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
        String expected = property + " property not found in " + value + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlParser.getTemplateValue(value)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
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
        String expected = property + " property not found in " + value + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlParser.getColumnName(value)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenTriplesMapResource_WhenGetTriplesMapIdName_ThenReturnString() {
        Resource value = model.createResource("RESOURCE");
        String result = r2rmlParser.getTriplesMapIdName(value);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenRefObjectMapResource_WhenCheckIsRefObjectMap_ThenReturnTrue() {
        Property property = R2RMLSyntax.parentTriplesMap;
        RDFNode blank = model.createResource();
        Resource value = model.createResource("RESOURCE").addProperty(property, blank);

        Boolean result = r2rmlParser.isRefObjectMap(value);
        assertThat(result, is(true));
    }

    @Test
    public void GivenNoRefObjectMapResource_WhenCheckIsRefObjectMap_ThenReturnFalse() {
        Resource value = model.createResource("RESOURCE");
        Boolean result = r2rmlParser.isRefObjectMap(value);
        assertThat(result, is(false));
    }

    @Test
    public void GivenResourceWithParentTriplesMap_WhenGetParentTriplesMap_ThenReturnResource() {
        Property property = R2RMLSyntax.parentTriplesMap;
        RDFNode blank = model.createResource();
        Resource value = model.createResource("RESOURCE").addProperty(property, blank);

        Resource result = r2rmlParser.getParentTriplesMap(value);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenResourceWithNoParentTriplesMap_WhenGetParentTriplesMap_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property property = R2RMLSyntax.parentTriplesMap;
        String expected = property + " property not found in " + value + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlParser.getParentTriplesMap(value)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenResourceWithJoinCondition_WhenCheckHasJoinCondition_ThenReturnTrue() {
        Property property = R2RMLSyntax.joinCondition;
        RDFNode blank = model.createResource();
        Resource value = model.createResource("RESOURCE").addProperty(property, blank);

        Boolean result = r2rmlParser.hasJoinCondition(value);
        assertThat(result, is(true));
    }

    @Test
    public void GivenResourceWithNoJoinCondition_WhenCheckHasJoinCondition_ThenReturnFalse() {
        Resource value = model.createResource("RESOURCE");
        Boolean result = r2rmlParser.hasJoinCondition(value);
        assertThat(result, is(false));
    }

    @Test
    public void GivenResourceWithJoinCondition_WhenListJoinCondition_ThenReturnSet() {
        Property property = R2RMLSyntax.joinCondition;
        RDFNode blank = model.createResource();
        Resource value = model.createResource("RESOURCE").addProperty(property, blank);

        Set<Resource> result = r2rmlParser.listJoinConditions(value);
        assertThat(result, is(not(empty())));
    }

    @Test
    public void GivenResourceWithNoJoinCondition_WhenListJoinCondition_ThenReturnEmptySet() {
        Resource value = model.createResource("RESOURCE");
        Set<Resource> result = r2rmlParser.listJoinConditions(value);
        assertThat(result, is(empty()));
    }

    @Test
    public void GivenResourceWithChild_WhenGetChildQuery_ThenReturnString() {
        Property property = R2RMLSyntax.child;
        Literal literal = model.createLiteral("CHILD");
        Resource value = model.createResource("RESOURCE").addProperty(property, literal);

        String result = r2rmlParser.getChildQuery(value);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenResourceWithNoChild_WhenGetChildQuery_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property property = R2RMLSyntax.child;
        String expected = property + " property not found in " + value + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlParser.getChildQuery(value)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenResourceWithParent_WhenGetParentQuery_ThenReturnString() {
        Property property = R2RMLSyntax.parent;
        Literal literal = model.createLiteral("PARENT");
        Resource value = model.createResource("RESOURCE").addProperty(property, literal);

        String result = r2rmlParser.getParentQuery(value);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenResourceWithNoParent_WhenGetParentQuery_ThenThrowException() {
        Resource value = model.createResource("RESOURCE");
        Property property = R2RMLSyntax.parent;
        String expected = property + " property not found in " + value + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlParser.getParentQuery(value)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }
}