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
import com.github.jiefenn8.graphloom.api.EntityMap;
import com.github.jiefenn8.graphloom.api.InputSource;
import com.github.jiefenn8.graphloom.api.RelationMap;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import com.github.jiefenn8.graphloom.rdf.r2rml.ObjectMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RDFMapperTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();
    @Mock InputSource mockInputSource;
    @Mock ConfigMaps mockConfigMaps;
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
        Model model = rdfMapper.mapToGraph(mockInputSource, mockConfigMaps);
        boolean result = model.isEmpty();

        assertThat(result, is(true));
    }

    @Test
    public void WhenMinimumTriplesMapGiven_ThenReturnModelWithSingleTriple() {
        //Override setup default
        EntityMap mockEntityMap = mock(EntityMap.class);
        when(mockConfigMaps.listEntityMaps()).thenReturn(ImmutableList.of(mockEntityMap));

        //Setup test expectations
        Resource expectedEntity = ResourceFactory.createResource("entity");
        Resource expectedClass = ResourceFactory.createResource("class");
        when(mockEntityMap.getSource()).thenReturn("TableName");
        when(mockInputSource.getEntityRecords(anyString())).thenReturn(ImmutableList.of(ImmutableMap.of()));
        when(mockEntityMap.generateEntityTerm(anyMap())).thenReturn(expectedEntity);
        when(mockEntityMap.listEntityClasses()).thenReturn(ImmutableList.of(expectedClass));

        //Act, Assert
        Model model = rdfMapper.mapToGraph(mockInputSource, mockConfigMaps);
        int result = model.listStatements().toList().size();

        assertThat(result, is(equalTo(1)));
    }

    @Test
    public void WhenMinimumTriplesMapGiven_ThenReturnModelWithRDFTypeTriple() {
        //Override setup default
        EntityMap mockEntityMap = mock(EntityMap.class);
        when(mockConfigMaps.listEntityMaps()).thenReturn(ImmutableList.of(mockEntityMap));

        //Setup test expectations
        Resource expectedEntity = ResourceFactory.createResource("entity");
        Resource expectedClass = ResourceFactory.createResource("class");
        Statement expected = ResourceFactory.createStatement(expectedEntity, RDF.type, expectedClass);

        when(mockEntityMap.getSource()).thenReturn("table");
        when(mockInputSource.getEntityRecords(anyString())).thenReturn(ImmutableList.of(ImmutableMap.of()));
        when(mockEntityMap.generateEntityTerm(anyMap())).thenReturn(expectedEntity);
        when(mockEntityMap.listEntityClasses()).thenReturn(ImmutableList.of(expectedClass));

        //Act, Assert
        Model model = rdfMapper.mapToGraph(mockInputSource, mockConfigMaps);
        List<Statement> result = model.listStatements().toList();

        assertThat(result, hasItem(expected));
    }

    @Test
    public void WhenTriplesMapWithPredicateObjectMapGiven_ThenReturnModelWithEntityProperty() {
        //Override setup default
        EntityMap mockEntityMap = mock(EntityMap.class);
        when(mockConfigMaps.listEntityMaps()).thenReturn(ImmutableList.of(mockEntityMap));

        //Setup test expectations
        Resource expectedEntity = ResourceFactory.createResource("entity");
        Property expectedProp = ResourceFactory.createProperty("relation");
        Literal expectedLiteral = ResourceFactory.createStringLiteral("literal");
        Statement expected = ResourceFactory.createStatement(expectedEntity, expectedProp, expectedLiteral);

        when(mockEntityMap.getSource()).thenReturn("table");
        when(mockInputSource.getEntityRecords(anyString())).thenReturn(ImmutableList.of(ImmutableMap.of()));
        when(mockEntityMap.generateEntityTerm(anyMap())).thenReturn(expectedEntity);

        RelationMap mockRelationMap = mock(RelationMap.class);
        when(mockEntityMap.listRelationMaps()).thenReturn(ImmutableSet.of(mockRelationMap));
        when(mockRelationMap.generateRelationTerm(anyMap())).thenReturn(expectedProp);

        ObjectMap mockObjectMap = mock(ObjectMap.class);
        when(mockEntityMap.getNodeMapWithRelation(mockRelationMap)).thenReturn(mockObjectMap);
        when(mockObjectMap.generateNodeTerm(anyMap())).thenReturn(expectedLiteral);

        //Act, Assert
        Model model = rdfMapper.mapToGraph(mockInputSource, mockConfigMaps);
        List<Statement> result = model.listStatements().toList();

        assertThat(result, hasItem(expected));
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
        rdfMapper.mapToGraph(mockInputSource, null);
    }
}