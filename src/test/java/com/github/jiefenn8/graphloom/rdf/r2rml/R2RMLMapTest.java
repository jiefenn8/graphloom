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

import com.github.jiefenn8.graphloom.api.EntityMap;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class R2RMLMapTest {

    @Mock private Map<String, String> mockNamespaceMap;
    private R2RMLMap r2rmlMap;

    @Test
    public void WhenNoNamespaceMapGiven_ThenReturnMap() {
        r2rmlMap = new R2RMLMap(mockNamespaceMap);
        Map<String, String> result = r2rmlMap.getNamespaceMap();
        assertThat(result, notNullValue());
    }

    @Test
    public void WhenNamespaceMapGiven_ThenReturnNonEmptyMap() {
        Map<String, String> namespaceMap = ImmutableMap.of("rr", "http://www.w3.org/ns/r2rml#");
        r2rmlMap = new R2RMLMap(namespaceMap);
        boolean result = r2rmlMap.getNamespaceMap().isEmpty();
        assertThat(result, is(false));
    }

    @Test
    public void WhenEntityMapGiven_ThenReturnNonEmptyList() {
        r2rmlMap = new R2RMLMap(mockNamespaceMap);
        r2rmlMap.addTriplesMap(mock(EntityMap.class));
        boolean result = r2rmlMap.listEntityMaps().isEmpty();
        assertThat(result, is(false));
    }
}