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

import org.apache.jena.rdf.model.Literal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ObjectMapTest {

    @Mock Map<String, String> mockRecord;
    private ObjectMap objectMap;

    @Before
    public void setUp() throws Exception {
        objectMap = new ObjectMap("Col_1_Name");
    }

    @Test
    public void WhenEntityRecordGiven_ThenReturnRDFNode() {
        String expectedValue = "Col_1_Val";
        when(mockRecord.get(anyString()))
                .thenReturn(expectedValue);

        Literal term = (Literal) objectMap.generateNodeTerm(mockRecord);
        String result = term.getString();
        assertThat(result, is(equalTo(expectedValue)));
    }
}