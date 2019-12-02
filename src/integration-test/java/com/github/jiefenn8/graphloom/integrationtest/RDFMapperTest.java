/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.integrationtest;

import com.github.jiefenn8.graphloom.exceptions.MapperException;
import com.github.jiefenn8.graphloom.rdf.RDFMapper;
import com.github.jiefenn8.graphloom.rdf.parser.R2RMLParser;
import com.github.jiefenn8.graphloom.rdf.r2rml.R2RMLMap;
import org.apache.jena.rdf.model.Model;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

//Simple all ups integration test.
public class RDFMapperTest {

    private final String validFile = "/r2rml/valid_r2rml.ttl";
    @Rule public ExpectedException exceptionRule = ExpectedException.none();
    private RDFMapper rdfMapper;
    private FakeInputDatabase fakeInputDatabase;
    private R2RMLMap mapperConfig;

    @Before
    public void SetUp() throws Exception {
        String path = getClass().getResource(validFile).getPath();
        R2RMLParser parser = new R2RMLParser();
        mapperConfig = parser.parse(path);
        rdfMapper = new RDFMapper();
        fakeInputDatabase = new FakeInputDatabase();
    }

    @Test
    public void WhenSourceAndConfigGiven_ShouldReturnPopulatedGraph() {
        Model graph = rdfMapper.mapToGraph(fakeInputDatabase, mapperConfig);
        //Graph should have 2 triples from the given input and configs.
        long result = graph.size();

        assertThat(result, is(equalTo(2L)));
    }

    @Test
    public void WhenNoInputSourceGiven_ThenThrowException() {
        String expected = "Cannot retrieve source data from null input source";
        exceptionRule.expect(MapperException.class);
        exceptionRule.expectMessage(expected);
        rdfMapper.mapToGraph(null, mapperConfig);
    }

    @Test
    public void WhenNoConfigMapsGiven_ThenThrowException() {
        String expected = "Cannot map source from null config maps.";
        exceptionRule.expect(MapperException.class);
        exceptionRule.expectMessage(expected);
        rdfMapper.mapToGraph(fakeInputDatabase, null);
    }
}
