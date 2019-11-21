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

import com.github.jiefenn8.graphloom.api.MutableRecord;
import com.github.jiefenn8.graphloom.api.Record;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PredicateMapTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    private PredicateMap predicateMap;
    @Mock private MutableRecord mockRecord;
    @Mock private TermMap mockTermMap;

    @Test
    public void GivenNoTermMap_WhenCreateInstance_ThenThrowException(){
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("Term map must not be null.");
        predicateMap = new PredicateMap(null);
    }

    //generateRelationTerm

    @Test
    public void GivenNoRecord_WhenGenerateRelationTerm_ThenThrowException(){
        when(mockTermMap.generateRDFTerm(isNull())).thenThrow(new NullPointerException("Record is null."));
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("Record is null.");
        predicateMap = new PredicateMap(mockTermMap);
        predicateMap.generateRelationTerm(null);
    }

    @Test
    public void GivenRecord_WhenGenerateRelationTerm_ThenReturnProperty(){
        Resource mockResource = mock(Resource.class);
        when(mockTermMap.generateRDFTerm(any(Record.class))).thenReturn(mockResource);
        when(mockResource.asResource()).thenReturn(mockResource);
        when(mockResource.getURI()).thenReturn("RELATION_TERM");
        predicateMap = new PredicateMap(mockTermMap);
        Property result = predicateMap.generateRelationTerm(mockRecord);
        assertThat(result, is(notNullValue()));
    }
}