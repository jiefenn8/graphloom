/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link PredicateMap}.
 */
@RunWith(MockitoJUnitRunner.class)
public class PredicateMapTest {

    private PredicateMap predicateMap;
    @Mock private Entity mockEntity;
    @Mock private TermMap mockTermMap;

    @Before
    public void setUp() {
        predicateMap = new PredicateMap(mockTermMap);
    }

    @Test
    public void GivenNoTermMap_WhenCreateInstance_ThenThrowException() {
        String expected = "Term map must not be null.";
        Throwable throwable = Assert.assertThrows(
                NullPointerException.class,
                () -> new PredicateMap(null)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    //generateRelationTerm

    @Test
    public void GivenNoEntity_WhenGenerateRelationTerm_ThenThrowException() {
        String expected = "Entity is null.";
        when(mockTermMap.generateRDFTerm(isNull())).thenThrow(new NullPointerException(expected));
        Throwable throwable = Assert.assertThrows(
                NullPointerException.class,
                () -> predicateMap.generateRelationTerm(null)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenEntity_WhenGenerateRelationTerm_ThenReturnProperty() {
        Resource mockResource = mock(Resource.class);
        when(mockTermMap.generateRDFTerm(any(Entity.class))).thenReturn(mockResource);
        when(mockResource.asResource()).thenReturn(mockResource);
        when(mockResource.getURI()).thenReturn("RELATION_TERM");
        Property result = predicateMap.generateRelationTerm(mockEntity);
        assertThat(result, is(notNullValue()));
    }
}