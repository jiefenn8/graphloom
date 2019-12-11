/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.parser;

import org.apache.jena.shared.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class R2RMLParserTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    private R2RMLParser r2rmlParser;

    @Before
    public void setUp() {
        r2rmlParser = new R2RMLParser();
    }

    @Test
    public void GivenValidFile_WhenParse_ThenReturnTrue() {
        boolean result = r2rmlParser.parse("r2rml/valid_r2rml.ttl", null);
        assertThat(result, is(true));
    }

    @Test
    public void GivenInvalidFile_WhenParse_ThenThrowException() {
        String invalidFile = "invalid_file.ttl";
        exceptionRule.expect(NotFoundException.class);
        r2rmlParser.parse(invalidFile, null);
    }
}
