/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;


import io.github.jiefenn8.graphloom.api.InputSource;
import io.github.jiefenn8.graphloom.api.SourceMap;
import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link TriplesMap}.
 */
@RunWith(MockitoJUnitRunner.class)
public class TriplesMapTest {

    private TriplesMap triplesMap;
    @Mock private LogicalTable mockLogicalTable;
    @Mock private SubjectMap mockSubjectMap;

    @Before
    public void setUp() {
        triplesMap = new TriplesMap.Builder("TEST_ID", mockLogicalTable, mockSubjectMap).build();
    }

    @Test
    public void Return_non_null_source_map() {
        SourceMap result = triplesMap.getSourceMap();
        assertThat(result, is(equalTo(mockLogicalTable)));
    }

    @Test
    public void Return_null_when_entity_value_is_null() {
        when(mockSubjectMap.generateEntityTerm(any())).thenReturn(null);
        Resource result = triplesMap.generateEntityTerm(mock(Entity.class));
        assertThat(result, is(nullValue()));
    }

    @Test
    public void Returns_true_when_there_is_predicate_object_map() {
        triplesMap = new TriplesMap.Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap)
                .addPredicateObjectMap(ImmutablePair.of(mock(PredicateMap.class), mock(ObjectMap.class)))
                .build();

        boolean result = triplesMap.hasNodeMapPairs();
        assertThat(result, is(true));
    }

    @Test
    public void Returns_false_when_there_is_no_predicate_object_map() {
        boolean result = triplesMap.hasNodeMapPairs();
        assertThat(result, is(false));
    }

    @Test
    public void Generate_model_with_term_entity_and_class() {
        when(mockSubjectMap.listEntityClasses()).thenReturn(List.of(ResourceFactory.createResource("CLASS")));
        Resource mockResource = ResourceFactory.createResource("TEST");
        Model model = triplesMap.generateClassTerms(mockResource, mock(Entity.class));
        NodeIterator iter = model.listObjectsOfProperty(RDF.type);
        int size = iter.toList().size();
        assertThat(size, is(1));
    }

    @Test
    public void Generate_model_with_term_and_entity_without_class() {
        when(mockSubjectMap.listEntityClasses()).thenReturn(List.of());
        Resource mockResource = ResourceFactory.createResource("TEST");
        Model model = triplesMap.generateClassTerms(mockResource, mock(Entity.class));
        NodeIterator iter = model.listObjectsOfProperty(RDF.type);
        int size = iter.toList().size();
        assertThat(size, is(0));
    }

    @Test
    public void Generate_relation_node_model_with_term_and_entity_with_predicate_object_map() {
        Resource mockResource = ResourceFactory.createResource("TEST");
        PredicateMap mockPredicateMap = mock(PredicateMap.class);
        ObjectMap mockObjectMap = mock(ObjectMap.class);
        Property propertyRef = ResourceFactory.createProperty("PROPERTY");
        when(mockPredicateMap.generateRelationTerm(any())).thenReturn(propertyRef);
        when(mockObjectMap.generateNodeTerm(any())).thenReturn(ResourceFactory.createStringLiteral("VALUE"));
        triplesMap = new TriplesMap.Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap)
                .addPredicateObjectMap(ImmutablePair.of(mockPredicateMap, mockObjectMap))
                .build();
        Model model = triplesMap.generateNodeTerms(mockResource, mock(Entity.class));
        NodeIterator iter = model.listObjectsOfProperty(propertyRef);
        int size = iter.toList().size();
        assertThat(size, is(1));
    }

    @Test
    public void Generate_relation_node_model_with_term_and_entity_without_predicate_object_map() {
        Resource mockResource = ResourceFactory.createResource("TEST");
        Property propertyRef = ResourceFactory.createProperty("PROPERTY");
        Model model = triplesMap.generateNodeTerms(mockResource, mock(Entity.class));
        NodeIterator iter = model.listObjectsOfProperty(propertyRef);
        int size = iter.toList().size();
        assertThat(size, is(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Generate_relation_node_model_with_term_and_entity_with_predicate_ref_object_map() {
        Resource mockResource = ResourceFactory.createResource("TEST");
        PredicateMap mockPredicateMap = mock(PredicateMap.class);
        RefObjectMap mockRefObjectMap = mock(RefObjectMap.class, RETURNS_MOCKS);
        Property propertyRef = ResourceFactory.createProperty("PROPERTY");
        when(mockPredicateMap.generateRelationTerm(any())).thenReturn(propertyRef);
        when(mockRefObjectMap.generateNodeTerm(any())).thenReturn(ResourceFactory.createResource("VALUE"));
        doAnswer(ans -> {
            Consumer<Entity> callback = (Consumer<Entity>) ans.getArguments()[1];
            callback.accept(mock(Entity.class));
            return null;
        }).when(mockLogicalTable).forEachEntity(any(), any(Consumer.class));
        when(mockLogicalTable.asJointLogicalTable(any())).thenReturn(mockLogicalTable);
        triplesMap = new TriplesMap.Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap)
                .addPredicateObjectMap(ImmutablePair.of(mockPredicateMap, mockRefObjectMap))
                .build();
        Model model = triplesMap.generateRefNodeTerms(mockResource, mock(InputSource.class));
        NodeIterator iter = model.listObjectsOfProperty(propertyRef);
        int size = iter.toList().size();
        assertThat(size, is(1));
    }

    @Test
    public void Generate_relation_node_model_with_term_and_entity_without_predicate_ref_object_map() {
        Resource mockResource = ResourceFactory.createResource("TEST");
        Property propertyRef = ResourceFactory.createProperty("PROPERTY");
        Model model = triplesMap.generateRefNodeTerms(mockResource, mock(InputSource.class));
        NodeIterator iter = model.listObjectsOfProperty(propertyRef);
        int size = iter.toList().size();
        assertThat(size, is(0));
    }

    @Test
    public void Return_absolute_unique_id_name() {
        String result = triplesMap.getUniqueId();
        assertThat(result.isEmpty(), is(false));
    }

    @Test
    public void Return_non_null_id_name() {
        String result = triplesMap.getIdName();
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void Create_instance_with_no_id_name_is_not_possible() {
        Assert.assertThrows(
                "ID name must not be null.",
                NullPointerException.class,
                () -> new TriplesMap.Builder(null, mockLogicalTable, mockSubjectMap)
        );
    }

    @Test
    public void Creating_instance_with_no_logical_table_is_not_possible() {
        Assert.assertThrows(
                "Logical table must not be null.",
                NullPointerException.class,
                () -> new TriplesMap.Builder(StringUtils.EMPTY, null, mockSubjectMap)
        );
    }

    @Test
    public void Creating_instance_with_no_subject_map_is_not_possible() {
        Assert.assertThrows(
                "Subject map must not be null.",
                NullPointerException.class,
                () -> new TriplesMap.Builder(StringUtils.EMPTY, mockLogicalTable, null)
        );
    }
}