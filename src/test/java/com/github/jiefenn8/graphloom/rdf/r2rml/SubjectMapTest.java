/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.Record;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@SubjectMap}.
 */
@RunWith(MockitoJUnitRunner.class)
public class SubjectMapTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    private SubjectMap subjectMap;
    @Mock private Record mockRecord;
    @Mock private TermMap mockTermMap;

    @Test
    public void GivenNoTermMap_WhenCreateInstance_ThenThrowException() {
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("Must provide a TermMap.");
        subjectMap = new SubjectMap(null);
    }

    //generateEntityTerm

    @Test
    public void GivenNoRecord_WhenGenerateEntityTerm_ThenPassException() {
        when(mockTermMap.generateRDFTerm(isNull())).thenThrow(new NullPointerException("Record is null."));
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("Record is null.");
        subjectMap = new SubjectMap(mockTermMap);
        subjectMap.generateEntityTerm(null);
    }

    @Test
    public void GivenRecord_WhenGenerateEntityTerm_ThenReturnResource() {
        Resource mockResource = mock(Resource.class);
        when(mockTermMap.generateRDFTerm(any(Record.class))).thenReturn(mockResource);
        when(mockResource.asResource()).thenReturn(mockResource);
        subjectMap = new SubjectMap(mockTermMap);
        Resource result = subjectMap.generateEntityTerm(mockRecord);
        assertThat(result, is(notNullValue()));
    }

    //addClass

    @Test(expected = NullPointerException.class)
    public void GivenNullResource_WhenAddClass_ThenThrowException() {
        subjectMap.addEntityClass(null);
    }

    //addEntityClass
    //listEntityClasses

    @Test
    public void WhenClassGiven_ThenReturnNonEmptyList() {
        Resource expected = ResourceFactory.createResource("resource");
        subjectMap = new SubjectMap(mockTermMap);
        subjectMap.addEntityClass(expected);
        boolean result = subjectMap.listEntityClasses().isEmpty();
        assertThat(result, is(false));
    }

    @Test
    public void WhenNoClassGiven_ThenReturnEmptyList() {
        subjectMap = new SubjectMap(mockTermMap);
        boolean result = subjectMap.listEntityClasses().isEmpty();
        assertThat(result, is(true));
    }
}