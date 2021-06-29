/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import io.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
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
 * Unit test class for {@link SubjectMap}.
 */
@RunWith(MockitoJUnitRunner.class)
public class SubjectMapTest {

    private SubjectMap subjectMap;
    @Mock private Entity mockEntity;
    @Mock private TermMap mockTermMap;

    @Before
    public void setUp() {
        subjectMap = new SubjectMap(mockTermMap);
    }

    @Test
    public void GivenNoTermMap_WhenCreateInstance_ThenThrowException() {
        String expected = "Must provide a TermMap.";
        Throwable throwable = Assert.assertThrows(
                NullPointerException.class,
                () -> new SubjectMap(null)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    //generateEntityTerm

    @Test
    public void GivenNoEntity_WhenGenerateEntityTerm_ThenPassException() {
        String expected = "Entity is null.";
        when(mockTermMap.generateRDFTerm(isNull())).thenThrow(new NullPointerException(expected));
        Throwable throwable = Assert.assertThrows(
                NullPointerException.class,
                () -> subjectMap.generateEntityTerm(null)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenEntity_WhenGenerateEntityTerm_ThenReturnResource() {
        Resource mockResource = mock(Resource.class);
        when(mockTermMap.generateRDFTerm(any(Entity.class))).thenReturn(mockResource);
        when(mockResource.asResource()).thenReturn(mockResource);

        Resource result = subjectMap.generateEntityTerm(mockEntity);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenEntity_WhenGenerateEntityTermIsLiteral_ThenThrowException() {
        Literal mockLiteral = mock(Literal.class);
        when(mockTermMap.generateRDFTerm(any(Entity.class))).thenReturn(mockLiteral);
        when(mockLiteral.isLiteral()).thenReturn(true);
        String expected = "SubjectMap can only return IRI or BlankNode.";
        Throwable throwable = Assert.assertThrows(
                MapperException.class,
                () -> subjectMap.generateEntityTerm(mockEntity)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    //addClass

    @Test
    public void GivenNoResource_WhenAddClass_ThenThrowException() {
        Assert.assertThrows(
                NullPointerException.class,
                () -> subjectMap.addEntityClass(null)
        );
    }

    //addEntityClass
    //listEntityClasses

    @Test
    public void WhenClassGiven_ThenReturnNonEmptyList() {
        Resource expected = ResourceFactory.createResource("resource");
        subjectMap.addEntityClass(expected);
        boolean result = subjectMap.listEntityClasses().isEmpty();
        assertThat(result, is(false));
    }

    @Test
    public void WhenNoClassGiven_ThenReturnEmptyList() {
        boolean result = subjectMap.listEntityClasses().isEmpty();
        assertThat(result, is(true));
    }
}