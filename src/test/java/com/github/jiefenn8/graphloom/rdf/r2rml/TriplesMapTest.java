/*
 *    Copyright (c) 2019 - Javen Liu (github.com/jiefenn8)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;


import com.github.jiefenn8.graphloom.api.NodeMap;
import com.github.jiefenn8.graphloom.api.RelationMap;
import org.apache.jena.rdf.model.Property;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TriplesMapTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();
    private TriplesMap triplesMap;
    @Mock private LogicalTable mockLogicalTable;
    @Mock private SubjectMap mockSubjectMap;

    @Test
    public void WhenPredicateAndObjectMapGiven_TheReturnNonEmptyList() {
        triplesMap = new TriplesMap(mockLogicalTable, mockSubjectMap);
        triplesMap.addRelationNodePair(mock(RelationMap.class), mock(NodeMap.class));
        boolean result = triplesMap.listRelationMaps().isEmpty();
        assertThat(result, is(false));
    }

    @Test
    public void WhenPredicateAndObjectMapGiven_ThenReturnTrue() {
        triplesMap = new TriplesMap(mockLogicalTable, mockSubjectMap);
        triplesMap.addRelationNodePair(mock(PredicateMap.class), mock(NodeMap.class));
        boolean result = triplesMap.hasRelationNodeMaps();
        assertThat(result, is(true));
    }

    @Test
    public void WhenPredicateMapGiven_ThenReturnNodeMap() {
        PredicateMap predicateMap = new PredicateMap(TermMap.TermMapType.CONSTANT, mock(Property.class));
        triplesMap = new TriplesMap(mockLogicalTable, mockSubjectMap);
        triplesMap.addRelationNodePair(predicateMap, mock(NodeMap.class));
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
        triplesMap.generateEntityTerm(anyMap());
        verify(mockSubjectMap, times(1)).generateEntityTerm(anyMap());
    }

    @Test
    public void WhenGetSource_ThenVerifyCall() {
        triplesMap = new TriplesMap(mockLogicalTable, mockSubjectMap);
        triplesMap.getSource();
        verify(mockLogicalTable, times(1)).getSource();
    }

    @Test
    public void WhenListEntityClasses_ThenVerifyCall() {
        triplesMap = new TriplesMap(mockLogicalTable, mockSubjectMap);
        triplesMap.listEntityClasses();
        verify(mockSubjectMap, times(1)).listEntityClasses();
    }

    //End
}