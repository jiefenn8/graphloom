/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.exceptions.MapperException;
import io.github.jiefenn8.graphloom.exceptions.ParserException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link R2RMLBuilder}.
 */
@RunWith(JUnitParamsRunner.class)
public class R2RMLBuilderTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    private static final String VALID_FILENAME = "r2rml_file.ttl";
    private static final String INVALID_FILENAME = "invalid_r2rml_file.ttl";

    @Mock private R2RMLParser mockR2rmlParser;
    @Mock private Resource mockResource;
    @Mock private Statement mockStatement;
    private R2RMLBuilder r2rmlBuilder;

    /**
     * Set up a fake graph emulating a valid r2rml file with a
     * single triples map including one predicate object map.
     * The following setup will assume that all term map are
     * constant type and source map are all base table or view.
     */
    @Before
    public void setUp() {
        //General setup. At least one TriplesMap as default.
        when(mockR2rmlParser.parse(anyString(), any())).thenReturn(true);
        when(mockR2rmlParser.getTriplesMaps()).thenReturn(Set.of(mockResource));

        //Setup of core triples map
        when(mockR2rmlParser.getTriplesMapIdName(mockResource)).thenReturn("TRIPLES_MAP");
        when(mockStatement.getSubject()).thenReturn(mockResource);
        when(mockStatement.getResource()).thenReturn(mockResource);
        when(mockR2rmlParser.getLogicalTable(any())).thenReturn(mockResource);
        when(mockR2rmlParser.getSubjectMap(any())).thenReturn(mockStatement);

        //Logical table setup
        when(mockR2rmlParser.isBaseTableOrView(any())).thenReturn(true);
        when(mockR2rmlParser.getTableName(any())).thenReturn("TABLE_NAME");

        //Term map setup
        when(mockR2rmlParser.isConstant(any())).thenReturn(true);
        when(mockR2rmlParser.getConstantValue(any())).thenReturn(mockResource);

        //Predicate object map setup
        when(mockR2rmlParser.getPredicateMap(any())).thenReturn(mockStatement);

        //SUT instance setup
        r2rmlBuilder = new R2RMLBuilder(mockR2rmlParser);
    }

    @Test
    @Parameters({"true, false, false", "false, true, true", "false, false, true"})
    public void Generate_R2RMLMap_with_term_map_types(boolean constant, boolean template, boolean column) {
        when(mockR2rmlParser.getTriplesMaps()).thenReturn(Set.of(mockResource));
        when(mockR2rmlParser.isConstant(any())).thenReturn(constant);
        when(mockR2rmlParser.isTemplate(any())).thenReturn(template);
        when(mockR2rmlParser.isColumn(any())).thenReturn(column);
        when(mockR2rmlParser.listPredicateObjectMaps(mockResource)).thenReturn(Set.of(mockStatement));
        when(mockR2rmlParser.getObjectMap(mockResource)).thenReturn(mockStatement);

        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(not(emptyIterable())));
    }

    @Test
    public void Generate_R2RMLMap_with_R2RMLView() {
        when(mockR2rmlParser.isBaseTableOrView(any())).thenReturn(false);
        when(mockR2rmlParser.isR2RMLView(any())).thenReturn(true);
        when(mockR2rmlParser.getSqlQuery(any())).thenReturn("SQL_QUERY");
        when(mockR2rmlParser.getVersion(any())).thenReturn("SQL_VERSION");

        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(not(emptyIterable())));
    }

    @Test
    public void Generate_empty_R2RMLMap_with_empty_file() {
        when(mockR2rmlParser.getTriplesMaps()).thenReturn(Set.of());

        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(emptyIterable()));
    }

    @Test
    public void Generate_R2RMLMap_with_TriplesMap() {
        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(not(emptyIterable())));
    }

    @Test
    public void Generate_R2RMLMap_with_RefObjectMap_and_Join() {
        Resource mockTriplesMapRes = mock(Resource.class);
        when(mockR2rmlParser.getTriplesMaps()).thenReturn(Set.of(mockResource, mockTriplesMapRes));
        when(mockR2rmlParser.listPredicateObjectMaps(mockResource)).thenReturn(Set.of(mockStatement));
        when(mockR2rmlParser.getObjectMap(mockResource)).thenReturn(mockStatement);
        when(mockR2rmlParser.isRefObjectMap(mockResource)).thenReturn(true);
        when(mockR2rmlParser.getParentTriplesMap(mockResource)).thenReturn(mockTriplesMapRes);
        when(mockR2rmlParser.getTriplesMapIdName(mockTriplesMapRes)).thenReturn("PARENT_TRIPLES_MAP");
        when(mockR2rmlParser.getLogicalTable(mockTriplesMapRes)).thenReturn(mockResource);
        when(mockR2rmlParser.getSubjectMap(mockTriplesMapRes)).thenReturn(mockStatement);
        when(mockR2rmlParser.listPredicateObjectMaps(mockTriplesMapRes)).thenReturn(Set.of());
        when(mockR2rmlParser.hasJoinCondition(any())).thenReturn(true);
        when(mockR2rmlParser.listJoinConditions(any())).thenReturn(Set.of(mockResource));
        when(mockR2rmlParser.getChildQuery(any())).thenReturn("CHILD_QUERY");
        when(mockR2rmlParser.getParentQuery(any())).thenReturn("PARENT_QUERY");

        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(not(emptyIterable())));
        verify(mockR2rmlParser, times(1)).hasJoinCondition(any());
    }

    @Test
    public void Parsing_RefObjectMap_with_circular_parent_reference_is_not_possible() {
        Resource mockTriplesMapRes = mock(Resource.class);
        when(mockR2rmlParser.getTriplesMaps()).thenReturn(Set.of(mockResource, mockTriplesMapRes));
        when(mockR2rmlParser.listPredicateObjectMaps(mockResource)).thenReturn(Set.of(mockStatement));
        when(mockR2rmlParser.getObjectMap(mockResource)).thenReturn(mockStatement);
        when(mockR2rmlParser.isRefObjectMap(mockResource)).thenReturn(true);
        when(mockR2rmlParser.getParentTriplesMap(mockResource)).thenReturn(mockTriplesMapRes);
        when(mockR2rmlParser.getTriplesMapIdName(mockTriplesMapRes)).thenReturn("PARENT_TRIPLES_MAP");
        when(mockR2rmlParser.getLogicalTable(mockTriplesMapRes)).thenReturn(mockResource);
        when(mockR2rmlParser.getSubjectMap(mockTriplesMapRes)).thenReturn(mockStatement);
        when(mockR2rmlParser.listPredicateObjectMaps(mockTriplesMapRes)).thenReturn(Set.of(mockStatement));

        String msg = "Circular dependency found in TRIPLES_MAP.";
        Assert.assertThrows(
                msg,
                MapperException.class,
                () -> r2rmlBuilder.parse(VALID_FILENAME)
        );
    }

    @Test
    public void Parsing_RefObjectMap_with_self_parent_reference_is_not_possible() {
        when(mockR2rmlParser.getObjectMap(mockResource)).thenReturn(mockStatement);
        when(mockR2rmlParser.isRefObjectMap(any())).thenReturn(true);
        when(mockR2rmlParser.getParentTriplesMap(mockResource)).thenReturn(mockResource);
        when(mockR2rmlParser.listPredicateObjectMaps(any())).thenReturn(Set.of(mockStatement));

        Assert.assertThrows(
                "RefObjectMap must not reference own parent " + mockResource + ".",
                ParserException.class,
                () -> r2rmlBuilder.parse(VALID_FILENAME)
        );
    }

    @Test
    public void Parsing_logical_table_with_no_source_config_is_not_possible() {
        when(mockR2rmlParser.isBaseTableOrView(any())).thenReturn(false);
        Assert.assertThrows(
                "No BaseTableOrView or R2RMLView property found.",
                ParserException.class,
                () -> r2rmlBuilder.parse(VALID_FILENAME)
        );
    }

    @Test
    public void Parsing_with_no_TermMap_is_not_possible() {
        when(mockR2rmlParser.isConstant(any())).thenReturn(false);
        Assert.assertThrows(
                mockStatement + " is not a TermMap.",
                ParserException.class,
                () -> r2rmlBuilder.parse(VALID_FILENAME)
        );
    }

    @Test
    public void Parsing_invalid_file_is_not_possible() {
        when(mockR2rmlParser.parse(any(), any())).thenReturn(false);
        Assert.assertThrows(
                "Failed to read file " + INVALID_FILENAME + ".",
                ParserException.class,
                () -> r2rmlBuilder.parse(INVALID_FILENAME)
        );
    }
}
