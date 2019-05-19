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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractEntityMapTest {

    private final String tableName = "EMP";
    private final String template = "http://data.example.com/employee/{EMPNO}";
    private final String classType = "http://example.com/ns#Employee";
    private EntityMap entityMap;
    @Mock
    private PredicateMap predicateMap;

    public abstract EntityMap createInstance();

    @Before
    public void setUp() throws Exception {
        entityMap = createInstance();
    }

    @Test
    public void WhenTableNameExists_ShouldReturnValue() {
        entityMap.setEntitySource(tableName);
        String result = entityMap.getEntitySource();
        assertThat(result, is(tableName));
    }

    @Test
    public void WhenTemplateExists_ShouldReturnValue() {
        entityMap.setTemplate(template);
        String result = entityMap.getTemplate();
        assertThat(result, is(template));
    }

    @Test
    public void WhenClassTypeExists_ShouldReturnValue() {
        entityMap.setClassType(classType);
        String result = entityMap.getClassType();
        assertThat(result, is(classType));
    }

    @Test
    public void WhenPredicateObjectMapExits_ShouldReturnList() {
        entityMap.addPredicateMap(predicateMap);
        List<PredicateMap> result = entityMap.listPredicateMaps();
        assertThat(result.isEmpty(), is(false));
    }
}