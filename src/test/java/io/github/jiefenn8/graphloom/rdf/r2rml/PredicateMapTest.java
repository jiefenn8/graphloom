/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import io.github.jiefenn8.graphloom.rdf.r2rml.AbstractTermMap.ValuedType;
import io.github.jiefenn8.graphloom.rdf.r2rml.PredicateMap.Builder;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link PredicateMap}.
 */
@RunWith(JUnitParamsRunner.class)
public class PredicateMapTest {

    @Rule public final MockitoRule mockitoRule = MockitoJUnit.rule();

    private PredicateMap predicateMap;
    @Mock private Entity mockEntity;

    public List<List<Object>> termMapValues() {
        return List.of(
                List.of(ValuedType.TEMPLATE,
                        ResourceFactory.createStringLiteral("data.example.com/{REFERENCE}"),
                        ResourceFactory.createProperty("data.example.com/VALUE")),
                List.of(ValuedType.COLUMN,
                        ResourceFactory.createStringLiteral("REFERENCE"),
                        ResourceFactory.createProperty("VALUE"))
        );
    }

    @Test
    @Parameters(method = "termMapValues")
    public void Generate_term_with_no_type(ValuedType valuedType, RDFNode base, RDFNode expected) {
        when(mockEntity.getPropertyValue(eq("REFERENCE"))).thenReturn("VALUE");
        Builder builder = new Builder(base, valuedType);
        predicateMap = builder.build();
        RDFNode result = predicateMap.generateRelationTerm(mockEntity);
        assertThat(result, is(equalTo(expected)));
    }

    public List<ValuedType> valuedTermValues() {
        return List.of(ValuedType.CONSTANT, ValuedType.COLUMN, ValuedType.TEMPLATE);
    }

    @Test
    @Parameters(method = "valuedTermValues")
    public void Generate_term_with_null_entity_is_not_possible(ValuedType valuedType) {
        Builder builder = new Builder(mock(RDFNode.class), valuedType);
        predicateMap = builder.build();
        Throwable throwable = Assert.assertThrows(
                "Entity is null.",
                NullPointerException.class,
                () -> predicateMap.generateRelationTerm(null)
        );
    }

    @Test
    @Parameters(method = "valuedTermValues")
    public void Generate_unique_id(ValuedType valuedType) {
        Builder builder = new Builder(mock(RDFNode.class), valuedType);
        predicateMap = builder.build();
        String result = predicateMap.getUniqueId();
        UUID uuid = UUID.fromString(result); //throws exception if invalid
        assertThat(result, is(equalTo(uuid.toString())));
    }

    @Test
    @Parameters(method = "valuedTermValues")
    public void Generate_json_string_of_object(ValuedType valuedType) {
        Builder builder = new Builder(mock(RDFNode.class), valuedType);
        predicateMap = builder.build();
        String result = predicateMap.toString();
        assertThat(result.isEmpty(), is(false));
    }
}