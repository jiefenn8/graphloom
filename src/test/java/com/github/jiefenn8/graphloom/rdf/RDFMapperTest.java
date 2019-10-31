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

package com.github.jiefenn8.graphloom.rdf;

import com.github.jiefenn8.graphloom.api.ConfigMaps;
import com.github.jiefenn8.graphloom.api.InputSource;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.jena.rdf.model.Model;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RDFMapperTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();
    @Mock private ConfigMaps mockConfigMaps;
    private RDFMapper rdfMapper;

    @Before
    public void setUp() throws Exception {
        //SUT instance creation
        rdfMapper = new RDFMapper();

        //Default mock behaviour setup
        when(mockConfigMaps.getNamespaceMap()).thenReturn(ImmutableMap.of());
        when(mockConfigMaps.listEntityMaps()).thenReturn(ImmutableList.of());
    }

    @Test
    public void WhenEmptyConfigGiven_ThenReturnEmptyGraph() {
        //Act, Assert
        Model model = rdfMapper.mapToGraph(mock(InputSource.class), mockConfigMaps);
        boolean result = model.isEmpty();

        assertThat(result, is(true));
    }

    @Test
    public void WhenNoInputSourceGiven_ThenThrowException() {
        String expected = "Cannot retrieve source data from null input source";
        exceptionRule.expect(MapperException.class);
        exceptionRule.expectMessage(expected);

        //Act, Assert
        rdfMapper.mapToGraph(null, mockConfigMaps);
    }

    @Test
    public void WhenNoConfigMapsGiven_ThenThrowException() {
        String expected = "Cannot map source from null config maps.";
        exceptionRule.expect(MapperException.class);
        exceptionRule.expectMessage(expected);

        //Act, Assert
        rdfMapper.mapToGraph(mock(InputSource.class), null);
    }
}