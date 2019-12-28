/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;


import com.github.jiefenn8.graphloom.api.MutableRecord;
import com.github.jiefenn8.graphloom.api.NodeMap;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TriplesMapTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    private TriplesMap triplesMap;
    @Mock private LogicalTable mockLogicalTable;
    @Mock private SubjectMap mockSubjectMap;

    @Before
    public void setUp() {
        //when(mockLogicalTable.withParentMap(any())).thenReturn(mockLogicalTable);
        when(mockSubjectMap.withParentMap(any())).thenReturn(mockSubjectMap);
    }

    @Test
    public void WhenPredicateAndObjectMapGiven_TheReturnNonEmptyList() {
        triplesMap = new TriplesMap
                .Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap)
                .addPredicateObjectMap(ImmutablePair.of(mock(PredicateMap.class), mock(ObjectMap.class)))
                .build();

        boolean result = triplesMap.listRelationMaps().isEmpty();

        assertThat(result, is(false));
    }

    @Test
    public void WhenPredicateAndObjectMapGiven_ThenReturnTrue() {
        triplesMap = new TriplesMap
                .Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap)
                .addPredicateObjectMap(ImmutablePair.of(mock(PredicateMap.class), mock(ObjectMap.class)))
                .build();

        boolean result = triplesMap.hasRelationNodeMaps();

        assertThat(result, is(true));
    }

    @Test
    public void WhenPredicateMapGiven_ThenReturnNodeMap() {
        PredicateMap mockPredicateMap = mock(PredicateMap.class);
        triplesMap = new TriplesMap
                .Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap)
                .addPredicateObjectMap(ImmutablePair.of(mockPredicateMap, mock(ObjectMap.class)))
                .build();

        NodeMap result = triplesMap.getNodeMapWithRelation(mockPredicateMap);

        assertThat(result, notNullValue());
    }

    @Test
    public void WhenNullLogicalTableGiven_ThrowException() {
        exceptionRule.expect(NullPointerException.class);
        triplesMap = new TriplesMap
                .Builder(StringUtils.EMPTY, null, mockSubjectMap)
                .build();
    }

    @Test
    public void WhenNullSubjectMapGiven_ThrowException() {
        exceptionRule.expect(NullPointerException.class);
        triplesMap = new TriplesMap
                .Builder(StringUtils.EMPTY, mockLogicalTable, null)
                .build();
    }

    //Test delegate method are called.

    @Test
    public void WhenGenerateEntity_ThenVerifyCall() {
        triplesMap = new TriplesMap
                .Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap)
                .build();

        triplesMap.generateEntityTerm(mock(MutableRecord.class));

        verify(mockSubjectMap, times(1)).generateEntityTerm(any());
    }

    @Test
    public void WhenListEntityClasses_ThenVerifyCall() {
        when(mockSubjectMap.listEntityClasses()).thenReturn(ImmutableList.of());
        triplesMap = new TriplesMap
                .Builder(StringUtils.EMPTY, mockLogicalTable, mockSubjectMap)
                .build();

        triplesMap.listEntityClasses();

        verify(mockSubjectMap, times(1)).listEntityClasses();
    }

    //End
}