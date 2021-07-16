/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.exceptions.MapperException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit test class for {@link RDFTermHelper}.
 */
@RunWith(JUnitParamsRunner.class)
public class RDFTermHelperTest {

    private final String value = "TERM_VALUE";

    public List<TermMap.TermType> termTypeParameters() {
        return List.of(TermMap.TermType.IRI, TermMap.TermType.LITERAL);
    }

    @Test
    @Parameters(method = "termTypeParameters")
    public void GivenNoValue_WhenCreateRDFTerm_ThenThrowException(TermMap.TermType type) {
        Assert.assertThrows(
                NullPointerException.class,
                () -> RDFTermHelper.asRDFTerm(null, type)
        );
    }

    @Test
    public void GivenUndefinedTermType_WhenCreateRDFTerm_ThenThrowException() {
        String expected = "Term type is UNDEFINED.";
        Throwable throwable = Assert.assertThrows(
                MapperException.class,
                () -> RDFTermHelper.asRDFTerm(value, TermMap.TermType.UNDEFINED)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenNoTermType_WhenCreateRDFTerm_ThenThrowException() {
        Assert.assertThrows(
                NullPointerException.class,
                () -> RDFTermHelper.asRDFTerm(value, null)
        );
    }

    @Test
    public void GivenValueWithIRIType_WhenCreateRDFTerm_ThenReturnResource() {
        RDFNode term = RDFTermHelper.asRDFTerm(value, TermMap.TermType.IRI);
        boolean result = term.isURIResource();
        assertThat(result, is(true));
    }

    @Test
    public void GivenValueWithLiteralType_WhenCreateRDFTerm_ThenReturnLiteral() {
        RDFNode term = RDFTermHelper.asRDFTerm(value, TermMap.TermType.LITERAL);
        boolean result = term.isLiteral();
        assertThat(result, is(true));
    }

    @Test
    public void GivenValueWithBlankType_WhenCreateRDFTerm_ThenReturnBlank() {
        RDFNode term = RDFTermHelper.asRDFTerm(value, TermMap.TermType.BLANK);
        boolean result = term.isAnon();
        assertThat(result, is(true));
    }
}