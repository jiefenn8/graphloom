/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.parser;

import com.github.jiefenn8.graphloom.rdf.r2rml.R2RMLMap;
import org.apache.jena.shared.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class R2RMLBuilderTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

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
        exceptionRule.expect(NotFoundException.class);

        String invalidFile = "invalid_file.ttl";
        r2rmlBuilder.parse(invalidFile, null);
    }
}
