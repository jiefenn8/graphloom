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

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PredicateMapTest {

    private PredicateMap predicateMap;

    @Before
    public void setUp() throws Exception {
        Property predicate = ResourceFactory.createProperty("Predicate_1");
        predicateMap = new PredicateMap(predicate);
    }

    @Test
    public void WhenRelationTermGiven_ThenReturnProperty() {
        Property expected = ResourceFactory.createProperty("Predicate_1");
        Property result = predicateMap.getRelationTerm();
        assertThat(result, is(equalTo(expected)));
    }
}