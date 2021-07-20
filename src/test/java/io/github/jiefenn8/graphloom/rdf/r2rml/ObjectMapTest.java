/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.api.NodeMap;
import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import io.github.jiefenn8.graphloom.rdf.r2rml.AbstractTermMap.ValuedType;
import io.github.jiefenn8.graphloom.rdf.r2rml.ObjectMap.Builder;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link ObjectMap}.
 */
@RunWith(JUnitParamsRunner.class)
public class ObjectMapTest {

    @Rule public final MockitoRule mockitoRule = MockitoJUnit.rule();

    private NodeMap objectMap;
    @Mock private Entity mockEntity;

    public List<List<Object>> termMapValues() {
        return List.of(
                List.of(ValuedType.TEMPLATE,
                        ResourceFactory.createStringLiteral("data.example.com/{REFERENCE}"),
                        ResourceFactory.createResource("data.example.com/VALUE")),
                List.of(ValuedType.COLUMN,
                        ResourceFactory.createStringLiteral("REFERENCE"),
                        ResourceFactory.createStringLiteral("VALUE"))
        );
    }

    @Test
    @Parameters(method = "termMapValues")
    public void Generate_term_with_no_type(ValuedType valuedType, RDFNode base, RDFNode expected){
        when(mockEntity.getPropertyValue(eq("REFERENCE"))).thenReturn("VALUE");
        Builder builder = new Builder(base, valuedType);
        objectMap = builder.build();
        RDFNode result = objectMap.generateNodeTerm(mockEntity);
        assertThat(result, is(equalTo(expected)));
    }

    public List<List<Object>> termMapValuesWithLanguage() {
        return List.of(
                List.of(ValuedType.TEMPLATE,
                        ResourceFactory.createStringLiteral("data.example.com/{REFERENCE}"),
                        ResourceFactory.createLangLiteral("data.example.com/VALUE", "en-us")),
                List.of(ValuedType.COLUMN,
                        ResourceFactory.createStringLiteral("REFERENCE"),
                        ResourceFactory.createLangLiteral("VALUE", "en-us"))
        );
    }

    @Test
    @Parameters(method = "termMapValuesWithLanguage")
    public void Generate_literal_with_language_tag(ValuedType valuedType, RDFNode base, RDFNode expected){
        when(mockEntity.getPropertyValue(eq("REFERENCE"))).thenReturn("VALUE");
        Builder builder = new Builder(base, valuedType);
        objectMap = builder.language("en-us").build();
        RDFNode result = objectMap.generateNodeTerm(mockEntity);
        assertThat(result, is(equalTo(expected)));
    }

    public List<List<Object>> termMapValuesWithDataType() {
        String dataType = "xsd:positiveInteger";
        RDFDatatype rdfDataType = NodeFactory.getType(dataType);
        return List.of(
                List.of(ValuedType.TEMPLATE,
                        ResourceFactory.createStringLiteral("data.example.com/{REFERENCE}"),
                        dataType,
                        ResourceFactory.createTypedLiteral("data.example.com/1234", rdfDataType)),
                List.of(ValuedType.COLUMN,
                        ResourceFactory.createStringLiteral("REFERENCE"),
                        dataType,
                        ResourceFactory.createTypedLiteral("1234", rdfDataType))
        );
    }

    @Test
    @Parameters(method = "termMapValuesWithDataType")
    public void Generate_literal_with_datatype(ValuedType valuedType, RDFNode base, String dataType, RDFNode expected){
        when(mockEntity.getPropertyValue(eq("REFERENCE"))).thenReturn("1234");
        Builder builder = new Builder(base, valuedType);
        objectMap = builder.dataType(dataType).build();
        RDFNode result = objectMap.generateNodeTerm(mockEntity);
        assertThat(result, is(equalTo(expected)));
    }

    public List<ValuedType> valuedTermValues() {
        return List.of(ValuedType.CONSTANT, ValuedType.COLUMN, ValuedType.TEMPLATE);
    }

    @Test
    @Parameters(method = "valuedTermValues")
    public void Generate_unique_id(ValuedType valuedType) throws Exception {
        Builder builder = new Builder(mock(RDFNode.class), valuedType);
        objectMap = builder.build();
        String result = objectMap.getUniqueId();
        UUID uuid = UUID.fromString(result); //throws exception if invalid
        assertThat(result, is(equalTo(uuid.toString())));
    }

    @Test
    @Parameters(method = "valuedTermValues")
    public void Generate_json_string_of_object(ValuedType valuedType) {
        Builder builder = new Builder(mock(RDFNode.class), valuedType);
        objectMap = builder.build();
        String result = objectMap.toString();
        assertThat(result.isEmpty(), is(false));
    }
}
