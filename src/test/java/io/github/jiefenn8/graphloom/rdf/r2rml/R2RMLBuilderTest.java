/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.exceptions.ParserException;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link R2RMLBuilder}.
 */
@RunWith(MockitoJUnitRunner.class)
public class R2RMLBuilderTest {

    private static final String VALID_FILENAME = "r2rml_file.ttl";
    private static final String INVALID_FILENAME = "invalid_r2rml_file.ttl";
    @Mock private R2RMLParser mockR2rmlParser;
    private R2RMLBuilder r2rmlBuilder;
    @Mock private Resource mockResource;
    @Mock private Statement mockStatement;

    /**
     * Setup a fake graph emulating a valid r2rml file with a
     * single triples map including one predicate object map.
     * The following setup will assume that all term map are
     * constant type and source map are all base table or view.
     */
    @Before
    public void setUp() {
        //General setup
        when(mockR2rmlParser.parse(anyString(), any())).thenReturn(true);
        when(mockR2rmlParser.getTriplesMaps()).thenReturn(Set.of(mockResource));

        //Setup of one triples map
        when(mockStatement.getSubject()).thenReturn(mockResource);
        when(mockStatement.getResource()).thenReturn(mockResource);
        when(mockR2rmlParser.getTriplesMapIdName(mockResource)).thenReturn("TRIPLES_MAP_1");
        when(mockR2rmlParser.getLogicalTable(any())).thenReturn(mockResource);
        when(mockR2rmlParser.getSubjectMap(any())).thenReturn(mockStatement);
        when(mockR2rmlParser.listPredicateObjectMaps(any())).thenReturn(Set.of(mockStatement));

        //Logical table setup
        when(mockR2rmlParser.isBaseTableOrView(any())).thenReturn(true);
        when(mockR2rmlParser.getTableName(any())).thenReturn("TABLE_NAME");

        //Term map setup
        when(mockR2rmlParser.isConstant(any())).thenReturn(true);
        when(mockR2rmlParser.getConstantValue(any())).thenReturn(mockResource);

        //Predicate object map setup
        when(mockR2rmlParser.getPredicateMap(any())).thenReturn(mockStatement);
        when(mockR2rmlParser.getObjectMap(any())).thenReturn(mockStatement);

        //SUT instance setup
        r2rmlBuilder = new R2RMLBuilder(mockR2rmlParser);
    }

    @Test
    public void GivenTemplateTermMap_WhenParse_ThenReturnR2RMLMap() {
        when(mockR2rmlParser.isConstant(any())).thenReturn(false);
        when(mockR2rmlParser.isTemplate(any())).thenReturn(true);
        when(mockR2rmlParser.getTemplateValue(any(Resource.class))).thenReturn("{TEMPLATE}");

        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenColumnTermMap_WhenParse_ThenReturnR2RMLMap() {
        when(mockR2rmlParser.isConstant(any())).thenReturn(false);
        when(mockR2rmlParser.isColumn(any())).thenReturn(true);
        when(mockR2rmlParser.getColumnName(any(Resource.class))).thenReturn("COLUMN_NAME");

        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenR2RMLView_WhenParse_ThenReturnR2RMLMap() {
        when(mockR2rmlParser.isBaseTableOrView(any())).thenReturn(false);
        when(mockR2rmlParser.isR2RMLView(any())).thenReturn(true);
        when(mockR2rmlParser.getSqlQuery(any())).thenReturn("SQL_QUERY");
        when(mockR2rmlParser.getVersion(any())).thenReturn("SQL_VERSION");

        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenEmptyFile_WhenParse_ThenReturnR2RMLMap() {
        when(mockR2rmlParser.getTriplesMaps()).thenReturn(Set.of());

        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenValidTriplesMap_WhenParse_ThenReturnR2RMLMap() {
        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(not(emptyIterable())));
    }

    @Test
    public void GivenRefObjectMapWithJoin_WhenParse_ThenReturnR2RMLMap() {
        Resource mockResource2 = mock(Resource.class);
        when(mockR2rmlParser.getTriplesMapIdName(mockResource2)).thenReturn("TRIPLES_MAP_2");
        when(mockR2rmlParser.isRefObjectMap(any())).thenReturn(true);
        when(mockR2rmlParser.getParentTriplesMap(any())).thenReturn(mockResource2);
        when(mockR2rmlParser.hasJoinCondition(any())).thenReturn(true);
        when(mockR2rmlParser.listJoinConditions(any())).thenReturn(Set.of(mock(Resource.class)));
        when(mockR2rmlParser.getChildQuery(any())).thenReturn("CHILD_QUERY");
        when(mockR2rmlParser.getParentQuery(any())).thenReturn("PARENT_QUERY");
        when(mockR2rmlParser.listPredicateObjectMaps(mockResource2)).thenReturn(Set.of());

        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(notNullValue()));
        verify(mockR2rmlParser, times(1)).hasJoinCondition(any());
    }

    @Test
    public void GivenRefObjectMap_WhenParse_ThenReturnR2RMLMap() {
        Resource mockResource2 = mock(Resource.class);
        when(mockR2rmlParser.getTriplesMapIdName(mockResource2)).thenReturn("TRIPLES_MAP_2");
        when(mockR2rmlParser.isRefObjectMap(any())).thenReturn(true);
        when(mockR2rmlParser.getParentTriplesMap(mockResource)).thenReturn(mockResource2);
        when(mockR2rmlParser.listPredicateObjectMaps(mockResource2)).thenReturn(Set.of());

        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenRefObjectMapWithOwnParentReference_WhenParse_ThenThrowException() {
        when(mockR2rmlParser.isRefObjectMap(any())).thenReturn(true);
        when(mockR2rmlParser.getParentTriplesMap(mockResource)).thenReturn(mockResource);
        String expected = "RefObjectMap must not reference own parent " + mockResource + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlBuilder.parse(VALID_FILENAME)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenRefObjectMapWithCircularDependency_WhenParse_ThenThrowException() {
        Resource mockResource2 = mock(Resource.class);
        Statement mockStatement2 = mock(Statement.class);
        when(mockStatement2.getSubject()).thenReturn(mockResource2);
        when(mockStatement2.getResource()).thenReturn(mockResource2);
        when(mockR2rmlParser.isRefObjectMap(any())).thenReturn(true);
        when(mockR2rmlParser.getParentTriplesMap(mockResource)).thenReturn(mockResource2);
        when(mockR2rmlParser.getTriplesMapIdName(mockResource2)).thenReturn("TRIPLE_MAP_2");
        when(mockR2rmlParser.listPredicateObjectMaps(mockResource2)).thenReturn(Set.of(mockStatement2));
        when(mockR2rmlParser.getObjectMap(mockResource2)).thenReturn(mockStatement2);
        when(mockR2rmlParser.getParentTriplesMap(mockResource2)).thenReturn(mockResource);
        String expected = "Potential circular dependency found for " + mockResource + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlBuilder.parse(VALID_FILENAME)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenRefObjectMapWithDiffQueryAndNoJoin_WhenParse_ThenThrowException() {
        Resource mockResource2 = mock(Resource.class);
        when(mockR2rmlParser.getTriplesMapIdName(mockResource2)).thenReturn("TRIPLES_MAP_2");
        when(mockR2rmlParser.isRefObjectMap(any())).thenReturn(true);
        when(mockR2rmlParser.getParentTriplesMap(mockResource)).thenReturn(mockResource2);
        when(mockR2rmlParser.listPredicateObjectMaps(mockResource2)).thenReturn(Set.of());
        when(mockR2rmlParser.getLogicalTable(mockResource2)).thenReturn(mockResource2);
        when(mockR2rmlParser.getTableName(mockResource2)).thenReturn("DIFF_TABLE_NAME");
        String expected = "Triples Maps queries do not match. Must provide join condition.";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlBuilder.parse(VALID_FILENAME)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenLogicalTableWithNoSourceConfig_WhenParse_ThenThrowException() {
        when(mockR2rmlParser.isBaseTableOrView(any())).thenReturn(false);
        String expected = "No BaseTableOrView or R2RMLView property found.";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlBuilder.parse(VALID_FILENAME)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenTermMapWithNo_WhenParse_ThenThrowException() {
        when(mockR2rmlParser.isConstant(any())).thenReturn(false);
        String expected = mockStatement + " is not a TermMap.";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlBuilder.parse(VALID_FILENAME)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenInvalidFile_WhenParse_ThenThrowException() {
        when(mockR2rmlParser.parse(any(), any())).thenReturn(false);
        String expected = "Failed to read file " + INVALID_FILENAME + ".";

        Throwable throwable = Assert.assertThrows(
                ParserException.class,
                () -> r2rmlBuilder.parse(INVALID_FILENAME)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }
}
