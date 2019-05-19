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

package com.github.jiefenn8.graphloom.io;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for MappingDocument code. Currently skeletal till further
 * iteration of the project or feature.
 */
public class MappingDocumentTest {

    private final String r2rmlFilename = "/r2rml_input_01.ttl";
    private MappingDocument mappingDocument;

    @Before
    public void setUp() throws Exception {
        mappingDocument = new MappingDocument(getClass().getResourceAsStream(r2rmlFilename));
    }


    /**
     * Tests that the MappingDocument returns a populated {@code Model} containing
     * the configuration mapping of the config file. Note: Only compares the structure
     * currently.
     */
    @Test
    public void WhenMappingDocumentExists_ShouldReturnGraph() {
        Model expected = ModelFactory.createDefaultModel().read(getClass().getResourceAsStream(r2rmlFilename), null, "TTL");

        Model result = mappingDocument.getMappingGraph();

        assertThat(result.isIsomorphicWith(expected), is(true));
    }
}
