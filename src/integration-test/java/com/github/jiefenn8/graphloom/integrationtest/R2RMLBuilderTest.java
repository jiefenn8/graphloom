/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.integrationtest;

import com.github.jiefenn8.graphloom.rdf.r2rml.R2RMLBuilder;
import com.github.jiefenn8.graphloom.rdf.r2rml.R2RMLMap;
import org.apache.jena.shared.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Integration test class for {@link R2RMLBuilder}.
 */
public class R2RMLBuilderTest {

    private R2RMLBuilder r2rmlBuilder;

    @Before
    public void setUp() {
        r2rmlBuilder = new R2RMLBuilder();
    }

    @Test
    public void GivenValidFile_WhenParse_ThenReturnR2RMLMap() {
        R2RMLMap result = r2rmlBuilder.parse("r2rml/valid_r2rml.ttl", null);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenInvalidFile_WhenParse_ThenThrowException() {
        String invalidFile = "invalid_file.ttl";
        Assert.assertThrows(
                NotFoundException.class,
                () -> r2rmlBuilder.parse(invalidFile, null)
        );
    }

    @Test
    public void GivenRefObjectMapWithJoin_WhenParse_ThenReturnR2RMLMap() {
        R2RMLMap result = r2rmlBuilder.parse("r2rml/refobjectmap/refobjectmap_via_join.ttl");
        assertThat(result, is(notNullValue()));
    }
}
