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

import com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermMapType;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PredicateMapTest {

    @Mock Map<String, String> mockRow;
    private PredicateMap predicateMap;

    @Before
    public void setUp(){
        when(mockRow.get("Col_1_Type")).thenReturn("Col_1_Val");
    }

    @Test
    public void WhenConstantTermMapTypeGiven_ThenReturnTermAsProperty() {
        Property constant = ResourceFactory.createProperty("Predicate_1");
        predicateMap = new PredicateMap(TermMapType.CONSTANT, constant);
        RDFNode result = predicateMap.getRelationTerm();
        assertThat(result, is(instanceOf(Property.class)));
    }

    @Test
    public void WhenTemplateTermMapTypeGiven_ThenReturnTermAsResource(){
        predicateMap = new PredicateMap(TermMapType.TEMPLATE, "Template/{Col_1_Type}");
        boolean result = predicateMap.getRelationTerm(mockRow).isResource();
        assertThat(result, is(true));
    }
}