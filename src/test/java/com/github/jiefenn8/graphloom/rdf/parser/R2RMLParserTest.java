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

package com.github.jiefenn8.graphloom.rdf.parser;

import com.github.jiefenn8.graphloom.exceptions.ParserException;
import com.github.jiefenn8.graphloom.rdf.r2rml.R2RMLMap;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.shared.NotFoundException;
import org.apache.jena.util.FileManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class R2RMLParserTest {

    private final String filename = "r2rml_file.ttl";
    @Rule public ExpectedException exceptionRule = ExpectedException.none();
    private R2RMLParser r2rmlParser;
    @Mock private FileManager mockFileManager;
    @Mock private Model mockGraph;

    @Before
    public void SetUp() {
        //SUT instance creation
        r2rmlParser = new R2RMLParser(mockFileManager);

        //Default mock behaviour setup
        when(mockFileManager.loadModel(anyString(), any(), anyString()))
                .thenReturn(mockGraph);

        when(mockGraph.getNsPrefixURI(anyString()))
                .thenReturn("URI");

        when(mockGraph.getProperty(anyString(), anyString()))
                .thenReturn(mock(Property.class));

        when(mockGraph.listResourcesWithProperty(any(Property.class)))
                .thenReturn(mock(ResIterator.class));
    }

    @Test
    public void WhenValidFileGiven_ThenReturnR2RMLMap() {
        R2RMLMap result = r2rmlParser.parse(filename, null);
        assertThat(result, notNullValue());
    }

    @Test
    public void WhenInvalidFilePathGiven_ThenThrowException() {
        String invalid_path = "invalid_filepath.ttl";
        when(mockFileManager.loadModel(anyString(), any(), anyString()))
                .thenThrow(NotFoundException.class);

        exceptionRule.expect(NotFoundException.class);
        r2rmlParser.parse(invalid_path);
    }

    @Test
    public void WhenNoNamespaceGiven_ThenThrowException() {
        String expectedMessage = "'rr' prefix uri not found.";
        when(mockGraph.getNsPrefixURI(anyString()))
                .thenReturn(null);

        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expectedMessage);
        r2rmlParser.parse(filename);
    }

    @Test
    public void WhenNoTriplesMapGiven_ThenThrowException() {
        String expected = "No valid Triples Map with rr:logicalTable found.";
        when(mockGraph.getProperty("rr", "logicalTable"))
                .thenReturn(null);

        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expected);
        r2rmlParser.parse(filename);
    }
}