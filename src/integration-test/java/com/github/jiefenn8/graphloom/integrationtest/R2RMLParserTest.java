/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.integrationtest;

import com.github.jiefenn8.graphloom.api.ConfigMaps;
import com.github.jiefenn8.graphloom.exceptions.ParserException;
import com.github.jiefenn8.graphloom.rdf.r2rml.R2RMLParser;
import com.github.jiefenn8.graphloom.rdf.r2rml.R2RMLMap;
import org.apache.jena.shared.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class R2RMLParserTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    private R2RMLParser r2rmlParser;

    @Before
    public void setUp() {
        r2rmlParser = new R2RMLParser();
    }

    @Test
    public void WhenValidFileGiven_ThenReturnR2RMLMap() {
        R2RMLMap result = r2rmlParser.parse("r2rml/valid_r2rml.ttl", null);
        assertThat(result, is(notNullValue()));//NOPMD
    }

    @Test
    public void WhenInvalidFilePathGiven_ThenThrowException() {
        String invalid_path = "invalid_filepath.ttl";
        exceptionRule.expect(NotFoundException.class);
        r2rmlParser.parse(invalid_path);
    }

    @Test
    public void GivenNoLogicalTableSub_WhenParse_ThenThrowException() {
        String invalidLtR2rmlFile = "r2rml/invalid_logical_table.ttl";
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage("No BaseTableOrView or R2RMLView property found.");
        r2rmlParser.parse(invalidLtR2rmlFile);
    }

    @Test
    public void GivenBaseTableOrView_WhenParse_ThenReturnConfigMaps() {
        String validBaseTableOrViewFile = "r2rml/valid_r2rml.ttl";
        ConfigMaps result = r2rmlParser.parse(validBaseTableOrViewFile);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenR2RMLView_WhenParse_ThenReturnConfigMaps() {
        String validR2RMLViewFile = "r2rml/valid_r2rml_view.ttl";
        ConfigMaps result = r2rmlParser.parse(validR2RMLViewFile);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenR2RMLViewWithNoSqlVersion_WhenParse_ThenThrowException() {
        String validR2RMLViewFile = "r2rml/invalid_r2rml_view.ttl";
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage("SqlVersion property not found with SqlQuery.");
        r2rmlParser.parse(validR2RMLViewFile);
    }

    @Test
    public void GivenR2RMLViewWithIRIReference_WhenParseThenReturnConfigMaps(){
        String r2rmlViewIRIRefFile = "r2rml/r2rml_view_iri_ref.ttl";
        ConfigMaps result = r2rmlParser.parse(r2rmlViewIRIRefFile);
        assertThat(result, is(notNullValue()));
    }

}
