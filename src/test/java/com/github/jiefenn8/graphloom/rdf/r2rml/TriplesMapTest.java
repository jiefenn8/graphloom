/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;


import com.github.jiefenn8.graphloom.api.NodeMap;
import com.github.jiefenn8.graphloom.api.inputsource.Entity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
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
        triplesMap = new TriplesMap.Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap).build();
    }

    @Test
    public void GivenPredicateObjectMap_WhenListRelationMaps_TheReturnNonEmptyList() {
        triplesMap = new TriplesMap.Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap)
                .addPredicateObjectMap(ImmutablePair.of(mock(PredicateMap.class), mock(ObjectMap.class)))
                .build();

        boolean result = triplesMap.listRelationMaps().isEmpty();
        assertThat(result, is(false));
    }

    @Test
    public void GivenPredicateAndObjectMap_WhenCheckHasRelationMap_ThenReturnTrue() {
        triplesMap = new TriplesMap.Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap)
                .addPredicateObjectMap(ImmutablePair.of(mock(PredicateMap.class), mock(ObjectMap.class)))
                .build();

        boolean result = triplesMap.hasRelationNodeMaps();
        assertThat(result, is(true));
    }

    @Test
    public void GivenPredicateMap_WhenGetNodeMapWithRelation_ThenReturnNodeMap() {
        PredicateMap mockPredicateMap = mock(PredicateMap.class);
        triplesMap = new TriplesMap.Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap)
                .addPredicateObjectMap(ImmutablePair.of(mockPredicateMap, mock(ObjectMap.class)))
                .build();

        NodeMap result = triplesMap.getNodeMapWithRelation(mockPredicateMap);
        assertThat(result, notNullValue());
    }

    @Test
    public void GivenNullLogicalTable_WhenCreateBuilder_ThrowException() {
        Assert.assertThrows(
                NullPointerException.class,
                () -> new TriplesMap.Builder(StringUtils.EMPTY, null, mockSubjectMap)
        );
    }

    @Test
    public void GivenNullSubjectMap_WhenCreateBuilder_ThrowException() {
        Assert.assertThrows(
                NullPointerException.class,
                () -> new TriplesMap.Builder(StringUtils.EMPTY, mockLogicalTable, null)
        );
    }

    //Test delegate method are called.

    @Test
    public void WhenGenerateEntity_ThenVerifyEntityCall() {
        triplesMap = new TriplesMap.Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap)
                .build();

        triplesMap.generateEntityTerm(mock(Entity.class));
        verify(mockSubjectMap, times(1)).generateEntityTerm(any(Entity.class));
    }

    @Test
    public void WhenListEntityClasses_ThenVerifyCall() {
        when(mockSubjectMap.listEntityClasses()).thenReturn(List.of());
        triplesMap = new TriplesMap.Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap)
                .build();

        triplesMap.listEntityClasses();
        verify(mockSubjectMap, times(1)).listEntityClasses();
    }

    //End
}