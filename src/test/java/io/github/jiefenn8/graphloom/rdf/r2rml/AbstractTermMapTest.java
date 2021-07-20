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
import org.apache.commons.lang3.StringUtils;
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
 * Unit test class for {@link ObjectMap}.
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

    public List<List<Object>> termMapValues() {
        return List.of(
                List.of(ValuedType.TEMPLATE,
                        ResourceFactory.createStringLiteral("data.example.com/{REFERENCE}"),
                        "VALUE",
                        ResourceFactory.createResource("data.example.com/VALUE")),
                List.of(ValuedType.CONSTANT,
                        ResourceFactory.createResource("data.example.com/VALUE"),
                        StringUtils.EMPTY,
                        ResourceFactory.createResource("data.example.com/VALUE")),
                List.of(ValuedType.COLUMN,
                        ResourceFactory.createStringLiteral("REFERENCE"),
                        "VALUE",
                        ResourceFactory.createResource("VALUE"))
        );
    }

    @Test
    @Parameters(method = "termMapValues")
    public void Generate_term_with_entity(ValuedType valuedType, RDFNode base, String value, RDFNode expected) {
        when(mockEntity.getPropertyValue(eq("REFERENCE"))).thenReturn(value);
        Builder builder = new Builder(base, valuedType);
        termMap = builder.build();
        RDFNode result = termMap.generateRDFTerm(mockEntity);
        assertThat(result, is(equalTo(expected)));
    }

    public class MockTermMap extends AbstractTermMap {

        public MockTermMap(AbstractBuilder builder) {
            super(builder);
        }

        @Override
        protected RDFNode handleDefaultGeneration(String term) {
            return asRDFTerm(term, TermType.IRI);
        }
    }

    public class Builder extends AbstractBuilder {

        public Builder(RDFNode baseValue, ValuedType valuedType) {
            super(baseValue, valuedType);
        }

        @Override
        public Builder termType(TermType type) {
            termType = type;
            return this;
        }

        @Override
        public MockTermMap build() {
            return new MockTermMap(this);
        }
    }
}