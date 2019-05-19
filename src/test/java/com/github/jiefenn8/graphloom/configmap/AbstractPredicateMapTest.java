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

package com.github.jiefenn8.graphloom.configmap;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for PredicateMap code. Currently skeletal till further
 * iteration of the project or feature.
 */
public abstract class AbstractPredicateMapTest {

    private final String predicate = "http://example.com/ns#name";
    private PredicateMap predicateMap;

    public abstract PredicateMap createInstance();

    @Before
    public void setUp() throws Exception {
        predicateMap = createInstance();
    }

    /**
     * Tests that the PredicateMap instance returns a valid predicate String
     * set by {@code PredicateMap}.
     */
    @Test
    public void WhenPredicateExists_ShouldReturnValue() {
        predicateMap.setPredicate(predicate);
        String result = predicateMap.getPredicate();
        assertThat(result, is(predicate));
    }
}
