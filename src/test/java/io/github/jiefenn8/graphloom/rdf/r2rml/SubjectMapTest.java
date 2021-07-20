/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static io.github.jiefenn8.graphloom.rdf.r2rml.AbstractTermMap.ValuedType;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit test class for {@link SubjectMap} and its Builder.
 */
@RunWith(MockitoJUnitRunner.class)
public class SubjectMapTest {

    private SubjectMap.Builder builder;
    @Mock private RDFNode mockBaseValue;

    @Before
    public void setUp() {
        builder = new SubjectMap.Builder(mockBaseValue, ValuedType.CONSTANT);
    }

    //addClass

    @Test
    public void GivenNoResource_WhenAddClass_ThenThrowException() {
        Assert.assertThrows(
                NullPointerException.class,
                () -> builder.addEntityClasses(null)
        );
    }

    //addEntityClass
    //listEntityClasses

    @Test
    public void WhenClassGiven_ThenReturnNonEmptyList() {
        Resource mockResource = mock(Resource.class);
        SubjectMap subjectMap = builder.addEntityClasses(Set.of(mockResource)).build();
        boolean result = subjectMap.listEntityClasses().contains(mockResource);
        assertThat(result, is(true));
    }

    @Test
    public void WhenNoClassGiven_ThenReturnEmptyList() {
        SubjectMap subjectMap = builder.build();
        boolean result = subjectMap.listEntityClasses().isEmpty();
        assertThat(result, is(true));
    }
}