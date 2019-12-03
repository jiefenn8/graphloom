/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf;

import com.github.jiefenn8.graphloom.exceptions.MapperException;
import com.github.jiefenn8.graphloom.FakeInputDatabase;
import com.github.jiefenn8.graphloom.rdf.parser.R2RMLBuilder;
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

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    private RDFMapper rdfMapper;
    private FakeInputDatabase fakeInputDatabase;
    private R2RMLMap mapperConfig;

    @Before
    public void SetUp() {
        String validFile = "/r2rml/valid_r2rml.ttl";
        String path = getClass().getResource(validFile).getPath();
        R2RMLBuilder r2rmlBuilder = new R2RMLBuilder();
        mapperConfig = r2rmlBuilder.parse(path);
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
