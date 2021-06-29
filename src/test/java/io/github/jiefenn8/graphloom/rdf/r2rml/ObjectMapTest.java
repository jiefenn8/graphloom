/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link ObjectMap}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ObjectMapTest {

    private ObjectMap objectMap;
    @Mock private Entity mockEntity;
    @Mock private TermMap mockTermMap;

    @Before
    public void setUp() {
        objectMap = new ObjectMap(mockTermMap);
        when(mockTermMap.generateRDFTerm(isNull())).thenThrow(new NullPointerException("Entity is null."));
    }

    @Test
    public void GivenNoTermMap_WhenCreateInstance_ThenThrowException() {
        String expected = "Term map must not be null.";
        Throwable throwable = Assert.assertThrows(
                NullPointerException.class,
                () -> new ObjectMap(null)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    //generateNodeTerm()

    @Test
    public void GivenNoEntity_WhenGenerateNodeTerm_ThenThrowException() {
        String expected = "Entity is null.";
        Throwable throwable = Assert.assertThrows(
                NullPointerException.class,
                () -> objectMap.generateNodeTerm(null)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenEntity_WhenGenerateNodeTerm_ThenReturnResource() {
        Resource mockResource = mock(Resource.class);
        when(mockTermMap.generateRDFTerm(mockEntity)).thenReturn(mockResource);
        RDFNode result = objectMap.generateNodeTerm(mockEntity);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenEntity_WhenGenerateNodeTerm_ThenReturnLiteral() {
        Literal mockLiteral = mock(Literal.class);
        when(mockTermMap.generateRDFTerm(mockEntity)).thenReturn(mockLiteral);
        RDFNode result = objectMap.generateNodeTerm(mockEntity);
        assertThat(result, is(notNullValue()));
    }
}