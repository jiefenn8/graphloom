/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.integrationtest;

import io.github.jiefenn8.graphloom.exceptions.MapperException;
import io.github.jiefenn8.graphloom.rdf.RDFMapper;
import io.github.jiefenn8.graphloom.rdf.r2rml.R2RMLBuilder;
import io.github.jiefenn8.graphloom.rdf.r2rml.R2RMLMap;
import org.apache.jena.rdf.model.Model;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration test class for {@link RDFMapper}.
 */
public class RDFMapperTest {

    private RDFMapper rdfMapper;
    private FakeInputDatabase fakeInputDatabase;
    private R2RMLMap mapperConfig;

    @Before
    public void setUp() {
        String validFile = "/r2rml/valid_r2rml.ttl";
        String path = getClass().getResource(validFile).getPath();
        R2RMLBuilder r2rmlBuilder = new R2RMLBuilder();
        mapperConfig = r2rmlBuilder.parse(path);
        rdfMapper = new RDFMapper();
        fakeInputDatabase = new FakeInputDatabase();
    }

    @Test
    public void WhenSourceAndConfigGiven_ShouldReturnPopulatedGraph() {
        //Expect 2 Triples from given inputs.
        Model graph = rdfMapper.mapToGraph(fakeInputDatabase, mapperConfig);
        long result = graph.size();
        assertThat(result, is(equalTo(3L)));
    }

    @Test
    public void WhenNoInputSourceGiven_ThenThrowException() {
        String expected = "Cannot retrieve source data from null input source.";
        Throwable throwable = Assert.assertThrows(
                MapperException.class,
                () -> rdfMapper.mapToGraph(null, mapperConfig)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void WhenNoConfigMapsGiven_ThenThrowException() {
        String expected = "Cannot map source from null config maps.";
        Throwable throwable = Assert.assertThrows(
                MapperException.class,
                () -> rdfMapper.mapToGraph(fakeInputDatabase, null)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }
}
