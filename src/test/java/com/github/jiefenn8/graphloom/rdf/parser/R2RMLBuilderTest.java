/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.parser;

import com.github.jiefenn8.graphloom.rdf.r2rml.R2RMLMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class R2RMLBuilderTest {

    private static final String VALID_FILENAME = "r2rml_file.ttl";
    @Mock R2RMLParser mockR2rmlParser;
    private R2RMLBuilder r2rmlBuilder;

    @Before
    public void setUp() {
        Resource mockResource = mock(Resource.class);
        Statement mockStatement = mock(Statement.class);
        when(mockR2rmlParser.parse(anyString(), any())).thenReturn(true);
        when(mockR2rmlParser.getNsPrefixMap()).thenReturn(ImmutableMap.of());
        when(mockR2rmlParser.getTriplesMap()).thenReturn(ImmutableList.of(mockResource));
        when(mockR2rmlParser.getLogicalTable(mockResource)).thenReturn(mockResource);
        when(mockR2rmlParser.isBaseTableOrView(mockResource)).thenReturn(true);
        when(mockR2rmlParser.getTableName(mockResource)).thenReturn("TABLE_NAME");
        when(mockR2rmlParser.getSubjectMap(mockResource)).thenReturn(mockStatement);
        when(mockR2rmlParser.getPredicateObjectMaps(mockResource)).thenReturn(ImmutableList.of());
        when(mockR2rmlParser.isConstant(mockStatement)).thenReturn(true);
        when(mockR2rmlParser.getConstantValue(mockStatement)).thenReturn(mockResource);

        r2rmlBuilder = new R2RMLBuilder(mockR2rmlParser);
    }

    @Test
    public void GivenEmptyFile_WhenParse_ThenReturnR2RMLMap() {
        when(mockR2rmlParser.getTriplesMap()).thenReturn(ImmutableList.of());

        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenFileWithTriplesMap_WhenParse_ThenReturnR2RMLMap() {
        R2RMLMap result = r2rmlBuilder.parse(VALID_FILENAME);
        assertThat(result, is(not(emptyIterable())));
    }
}
