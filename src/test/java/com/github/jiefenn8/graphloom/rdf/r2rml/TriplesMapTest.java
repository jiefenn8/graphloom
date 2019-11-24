/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;


import com.github.jiefenn8.graphloom.api.EntityMap;
import com.github.jiefenn8.graphloom.api.MutableRecord;
import com.github.jiefenn8.graphloom.api.NodeMap;
import org.apache.jena.rdf.model.Property;
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
        when(mockSubjectMap.withParentMap(any(EntityMap.class))).thenReturn(mockSubjectMap);
    }

    @Test
    public void WhenPredicateAndObjectMapGiven_TheReturnNonEmptyList() {
        triplesMap = new TriplesMap(mockLogicalTable, mockSubjectMap);
        triplesMap.addRelationNodePair(mock(PredicateMap.class), mock(ObjectMap.class));

        boolean result = triplesMap.listRelationMaps().isEmpty();

        assertThat(result, is(false));
    }

    @Test
    public void WhenPredicateAndObjectMapGiven_ThenReturnTrue() {
        triplesMap = new TriplesMap(mockLogicalTable, mockSubjectMap);
        triplesMap.addRelationNodePair(mock(PredicateMap.class), mock(ObjectMap.class));

        boolean result = triplesMap.hasRelationNodeMaps();

        assertThat(result, is(true));
    }

    @Test
    public void WhenPredicateMapGiven_ThenReturnNodeMap() {
        PredicateMap predicateMap = R2RMLFactory.createConstPredicateMap(mock(Property.class));
        ObjectMap mockObjectMap = mock(ObjectMap.class);
        when(mockObjectMap.withParentMap(any(EntityMap.class))).thenReturn(mockObjectMap);
        triplesMap = new TriplesMap(mockLogicalTable, mockSubjectMap);
        triplesMap.addRelationNodePair(predicateMap, mockObjectMap);

        NodeMap result = triplesMap.getNodeMapWithRelation(predicateMap);

        assertThat(result, notNullValue());
    }

    @Test
    public void WhenNullLogicalTableGiven_ThrowException() {
        exceptionRule.expect(NullPointerException.class);
        triplesMap = new TriplesMap(null, mockSubjectMap);
    }

    @Test
    public void WhenNullSubjectMapGiven_ThrowException() {
        exceptionRule.expect(NullPointerException.class);
        triplesMap = new TriplesMap(mockLogicalTable, null);
    }

    //Test delegate method are called.

    @Test
    public void WhenGenerateEntity_ThenVerifyCall() {
        triplesMap = new TriplesMap(mockLogicalTable, mockSubjectMap);
        triplesMap.generateEntityTerm(mock(MutableRecord.class));

        verify(mockSubjectMap, times(1)).generateEntityTerm(any());
    }

    @Test
    public void WhenListEntityClasses_ThenVerifyCall() {
        triplesMap = new TriplesMap(mockLogicalTable, mockSubjectMap);
        triplesMap.listEntityClasses();

        verify(mockSubjectMap, times(1)).listEntityClasses();
    }

    //End
}