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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class TriplesMapTest {

    private TriplesMap triplesMap;

    @Test
    public void WhenPredicateAndObjectMapGiven_TheReturnNonEmptyList() {
        triplesMap = new TriplesMap(null, null);
        triplesMap.addRelationNodePair(mock(RelationMap.class), mock(NodeMap.class));
        boolean result = triplesMap.listRelationMaps().isEmpty();
        assertThat(result, is(false));
    }

    public void WhenPredicateAndObjectMapGiven_ThenReturnTrue() {
        triplesMap = new TriplesMap(null, null);
        triplesMap.addRelationNodePair(mock(PredicateMap.class), mock(NodeMap.class));
        boolean result = triplesMap.hasRelationNodeMaps();
        assertThat(result, is(true));
    }

    public void WhenPredicateMapGiven_ThenReturnNodeMap() {
        PredicateMap predicateMap = new PredicateMap(TermMap.TermMapType.CONSTANT, mock(Property.class));
        triplesMap = new TriplesMap(null, null);
        triplesMap.addRelationNodePair(predicateMap, mock(NodeMap.class));
        NodeMap result = triplesMap.getNodeMapWithRelation(predicateMap);
        assertThat(result, notNullValue());
    }

}