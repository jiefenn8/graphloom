/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import io.github.jiefenn8.graphloom.rdf.r2rml.SubjectMap.Builder;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static io.github.jiefenn8.graphloom.rdf.r2rml.AbstractTermMap.ValuedType;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link SubjectMap} and its Builder.
 */
@RunWith(JUnitParamsRunner.class)
public class SubjectMapTest {

    @Rule public final MockitoRule mockitoRule = MockitoJUnit.rule();

    private SubjectMap subjectMap;
    @Mock private Entity mockEntity;

    public List<List<Object>> termMapValues() {
        return List.of(
                List.of(ValuedType.TEMPLATE,
                        ResourceFactory.createStringLiteral("data.example.com/{REFERENCE}"),
                        ResourceFactory.createResource("data.example.com/VALUE")),
                List.of(ValuedType.COLUMN,
                        ResourceFactory.createStringLiteral("REFERENCE"),
                        ResourceFactory.createResource("VALUE"))
        );
    }

    @Test
    @Parameters(method = "termMapValues")
    public void Generate_term_with_no_type(ValuedType valuedType, RDFNode base, RDFNode expected) {
        when(mockEntity.getPropertyValue(eq("REFERENCE"))).thenReturn("VALUE");
        Builder builder = new Builder(base, valuedType);
        subjectMap = builder.build();
        RDFNode result = subjectMap.generateEntityTerm(mockEntity);
        assertThat(result, is(equalTo(expected)));
    }

    public List<ValuedType> valuedTermValues() {
        return List.of(ValuedType.CONSTANT, ValuedType.COLUMN, ValuedType.TEMPLATE);
    }

    //addClass

    @Test
    @Parameters(method = "valuedTermValues")
    public void Add_null_class_is_not_possible(ValuedType valuedType) {
        Builder builder = new SubjectMap.Builder(mock(RDFNode.class), valuedType);
        Assert.assertThrows(
                NullPointerException.class,
                () -> builder.addEntityClasses(null)
        );
    }

    //addEntityClass
    //listEntityClasses

    @Test
    @Parameters(method = "valuedTermValues")
    public void WhenClassGiven_ThenReturnNonEmptyList(ValuedType valuedType) {
        Builder builder = new SubjectMap.Builder(mock(RDFNode.class), valuedType);
        Resource mockResource = mock(Resource.class);
        subjectMap = builder.addEntityClasses(Set.of(mockResource)).build();
        boolean result = subjectMap.listEntityClasses().contains(mockResource);
        assertThat(result, is(true));
    }

    @Test
    @Parameters(method = "valuedTermValues")
    public void WhenNoClassGiven_ThenReturnEmptyList(ValuedType valuedType) {
        Builder builder = new SubjectMap.Builder(mock(RDFNode.class), valuedType);
        subjectMap = builder.build();
        boolean result = subjectMap.listEntityClasses().isEmpty();
        assertThat(result, is(true));
    }

    @Test
    @Parameters(method = "valuedTermValues")
    public void Generate_unique_id(ValuedType valuedType) throws Exception {
        Builder builder = new Builder(mock(RDFNode.class), valuedType);
        subjectMap = builder.build();
        String result = subjectMap.getUniqueId();
        UUID uuid = UUID.fromString(result); //throws exception if invalid
        assertThat(result, is(equalTo(uuid.toString())));
    }

    @Test
    @Parameters(method = "valuedTermValues")
    public void Generate_json_string_of_object(ValuedType valuedType) {
        Builder builder = new Builder(mock(RDFNode.class), valuedType);
        subjectMap = builder.build();
        String result = subjectMap.toString();
        assertThat(result.isEmpty(), is(false));
    }
}