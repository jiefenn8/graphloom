/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test interface for {@link TermMap}.
 */
@RunWith(JUnitParamsRunner.class)
public class TermMapTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private Entity mockEntity;

    @Before
    public void setUp() {
        when(mockEntity.getPropertyValue(any())).thenReturn("VALUE");
    }

    public List<TermMap> termMapParameters() {
        return List.of(
                new ConstTermMap(mock(RDFNode.class)),
                new ColumnTermMap("COLUMN_NAME", TermMap.TermType.UNDEFINED),
                new TemplateTermMap("{COLUMN_NAME}", TermMap.TermType.UNDEFINED)
        );
    }

    @Test
    @Parameters(method = "termMapParameters")
    public void GivenTermMap_WhenGenerateRDFTerm_ThenReturnRDFNode(TermMap termMap) {
        RDFNode result = termMap.generateRDFTerm(mockEntity);
        assertThat(result, is(notNullValue()));
    }
}
