/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static io.github.jiefenn8.graphloom.rdf.r2rml.AbstractTermMap.*;
import static org.hamcrest.CoreMatchers.equalTo;
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

    //generateEntityTerm

    @Test
    public void GivenNoEntity_WhenGenerateEntityTerm_ThenPassException() {
        String expected = "Entity is null.";
        SubjectMap subjectMap = builder.build();
        Throwable throwable = Assert.assertThrows(
                NullPointerException.class,
                () -> subjectMap.generateEntityTerm(null)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenLiteralTermType_WhenBuilderSetTermType_ThenThrowException() {
        String expected = "SubjectMap can only generate IRI or BlankNode.";
        Throwable throwable = Assert.assertThrows(
                MapperException.class,
                () -> builder.termType(TermMap.TermType.LITERAL)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
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