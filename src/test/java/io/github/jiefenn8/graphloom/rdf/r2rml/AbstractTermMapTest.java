/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import io.github.jiefenn8.graphloom.rdf.r2rml.AbstractTermMap.AbstractBuilder;
import io.github.jiefenn8.graphloom.rdf.r2rml.AbstractTermMap.ValuedType;
import io.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermType;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link AbstractTermMap}.
 */
@RunWith(JUnitParamsRunner.class)
public class AbstractTermMapTest {

    @Rule public final MockitoRule mockitoRule = MockitoJUnit.rule();

    private MockTermMap termMap;
    @Mock private Entity mockEntity;
    @Mock private RDFNode mockRDFNode;

    public List<ValuedType> valuedTermValues() {
        return List.of(ValuedType.CONSTANT, ValuedType.COLUMN, ValuedType.TEMPLATE);
    }

    @Test
    @Parameters(method = "valuedTermValues")
    public void Generate_term_without_entity_is_not_possible(ValuedType valuedType) {
        Builder builder = new Builder(mockRDFNode, valuedType);
        termMap = builder.build();
        Assert.assertThrows(
                "Entity is null.",
                NullPointerException.class,
                () -> termMap.generateRDFTerm(null)
        );
    }

    public List<List<Object>> termMapConstantValues() {
        String value = "VALUE";
        RDFNode blank = ResourceFactory.createResource();
        return List.of(
                List.of(ResourceFactory.createResource(value),
                        TermType.IRI,
                        ResourceFactory.createResource(value)),
                List.of(ResourceFactory.createResource(value),
                        TermType.LITERAL,
                        ResourceFactory.createResource(value)),
                List.of(ResourceFactory.createResource(value),
                        TermType.BLANK,
                        ResourceFactory.createResource(value)),
                List.of(ResourceFactory.createStringLiteral(value),
                        TermType.IRI,
                        ResourceFactory.createStringLiteral(value)),
                List.of(ResourceFactory.createStringLiteral(value),
                        TermType.LITERAL,
                        ResourceFactory.createStringLiteral(value)),
                List.of(ResourceFactory.createStringLiteral(value),
                        TermType.BLANK,
                        ResourceFactory.createStringLiteral(value)),
                List.of(blank, TermType.IRI, blank),
                List.of(blank, TermType.LITERAL, blank),
                List.of(blank, TermType.BLANK, blank)
        );
    }

    @Test
    @Parameters(method = "termMapConstantValues")
    public void Constant_term_map_ignores_specified_type(RDFNode base, TermType termType, RDFNode expected) {
        Builder builder = new Builder(base, ValuedType.CONSTANT);
        termMap = builder.termType(termType).build();
        RDFNode result = termMap.generateRDFTerm(mockEntity);
        assertThat(result, is(equalTo(expected)));
    }

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
    public void Generate_term_with_entity(ValuedType valuedType, RDFNode base, RDFNode expected) {
        when(mockEntity.getPropertyValue(eq("REFERENCE"))).thenReturn("VALUE");
        Builder builder = new Builder(base, valuedType);
        termMap = builder.build();
        RDFNode result = termMap.generateRDFTerm(mockEntity);
        assertThat(result, is(equalTo(expected)));
    }

    public List<List<Object>> termMapValuesWithBlankType() {
        return List.of(
                List.of(ValuedType.TEMPLATE, ResourceFactory.createStringLiteral("data.example.com/{REFERENCE}")),
                List.of(ValuedType.COLUMN, ResourceFactory.createStringLiteral("REFERENCE"))
        );
    }

    @Test
    @Parameters(method = "termMapValuesWithBlankType")
    public void Generate_term_with_blank_type_should_return_blank_node(ValuedType valuedType, RDFNode base) {
        when(mockEntity.getPropertyValue(eq("REFERENCE"))).thenReturn("VALUE");
        Builder builder = new Builder(base, valuedType);
        termMap = builder.termType(TermType.BLANK).build();
        boolean result = termMap.generateRDFTerm(mockEntity).isAnon();
        assertThat(result, is(true));
    }

    public List<List<Object>> termMapValuesWithNonBlankType() {
        return List.of(
                List.of(ValuedType.TEMPLATE,
                        ResourceFactory.createStringLiteral("data.example.com/{REFERENCE}"),
                        TermType.IRI,
                        ResourceFactory.createResource("data.example.com/VALUE")),
                List.of(ValuedType.TEMPLATE,
                        ResourceFactory.createStringLiteral("data.example.com/{REFERENCE}"),
                        TermType.LITERAL,
                        ResourceFactory.createStringLiteral("data.example.com/VALUE")),
                List.of(ValuedType.COLUMN,
                        ResourceFactory.createStringLiteral("REFERENCE"),
                        TermType.IRI,
                        ResourceFactory.createResource("VALUE")),
                List.of(ValuedType.COLUMN,
                        ResourceFactory.createStringLiteral("REFERENCE"),
                        TermType.LITERAL,
                        ResourceFactory.createStringLiteral("VALUE"))
        );
    }

    @Test
    @Parameters(method = "termMapValuesWithNonBlankType")
    public void Generate_term_using_non_blank_type(ValuedType valuedType, RDFNode base, TermType type, RDFNode expected) {
        when(mockEntity.getPropertyValue(eq("REFERENCE"))).thenReturn("VALUE");
        Builder builder = new Builder(base, valuedType);
        termMap = builder.termType(type).build();
        RDFNode result = termMap.generateRDFTerm(mockEntity);
        assertThat(result, is(equalTo(expected)));
    }

    public class MockTermMap extends AbstractTermMap {

        public MockTermMap(Builder builder) {
            super(builder);
        }

        @Override
        protected RDFNode handleDefaultGeneration(String term) {
            return asRDFTerm(term, TermType.IRI);
        }
    }

    public class Builder extends AbstractBuilder<MockTermMap> {

        public Builder(RDFNode baseValue, ValuedType valuedType) {
            super(baseValue, valuedType);
        }

        @Override
        public MockTermMap build() {
            return new MockTermMap(this);
        }
    }
}