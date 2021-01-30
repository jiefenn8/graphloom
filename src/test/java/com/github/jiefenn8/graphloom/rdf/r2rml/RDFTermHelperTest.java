/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.exceptions.MapperException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.List;

import static com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermType;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit test class for {@link RDFTermHelper}.
 */
@RunWith(JUnitParamsRunner.class)
public class RDFTermHelperTest {

    private final String value = "TERM_VALUE";
    @Rule public ExpectedException expectedException = ExpectedException.none();

    public List<TermType> termTypeParameters() {
        return List.of(TermType.IRI, TermType.LITERAL);
    }

    @Test
    @Parameters(method = "termTypeParameters")
    public void GivenNoValue_WhenCreateRDFTerm_ThenThrowException(TermType t) {
        expectedException.expect(NullPointerException.class);
        RDFTermHelper.asRDFTerm(null, t);
    }

    @Test
    public void GivenUndefinedTermType_WhenCreateRDFTerm_ThenThrowException() {
        expectedException.expect(MapperException.class);
        expectedException.expectMessage("Term type is UNDEFINED.");
        RDFTermHelper.asRDFTerm(value, TermType.UNDEFINED);
    }

    @Test
    public void GivenNoTermType_WhenCreateRDFTerm_ThenThrowException() {
        expectedException.expect(NullPointerException.class);
        RDFTermHelper.asRDFTerm(value, null);
    }

    @Test
    public void GivenValueWithIRIType_WhenCreateRDFTerm_ThenReturnResource() {
        RDFNode term = RDFTermHelper.asRDFTerm(value, TermType.IRI);
        boolean result = term.isURIResource();
        assertThat(result, is(true));
    }

    @Test
    public void GivenValueWithLiteralType_WhenCreateRDFTerm_ThenReturnLiteral() {
        RDFNode term = RDFTermHelper.asRDFTerm(value, TermType.LITERAL);
        boolean result = term.isLiteral();
        assertThat(result, is(true));
    }

    @Test
    public void GivenValueWithBlankType_WhenCreateRDFTerm_ThenReturnBlank() {
        RDFNode term = RDFTermHelper.asRDFTerm(value, TermType.BLANK);
        boolean result = term.isAnon();
        assertThat(result, is(true));
    }
}