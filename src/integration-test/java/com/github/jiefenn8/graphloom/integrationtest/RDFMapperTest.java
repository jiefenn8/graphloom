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

package com.github.jiefenn8.graphloom.integrationtest;

import com.github.jiefenn8.graphloom.exceptions.MapperException;
import com.github.jiefenn8.graphloom.rdf.RDFMapper;
import com.github.jiefenn8.graphloom.rdf.parser.R2RMLParser;
import com.github.jiefenn8.graphloom.rdf.r2rml.R2RMLMap;
import org.apache.jena.rdf.model.Model;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

//Simple big bang integration test. todo: Refactor test to include more integration test.
public class RDFMapperTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    private final String validFile = "/valid_r2rml.ttl";
    private RDFMapper rdfMapper;
    private FakeInputDatabase fakeInputDatabase;
    private R2RMLMap mapperConfig;

    @Before
    public void SetUp() throws Exception
    {
        String path = getClass().getResource(validFile).getPath();
        R2RMLParser parser = new R2RMLParser();
        mapperConfig = parser.parse(path);
        rdfMapper = new RDFMapper();
        fakeInputDatabase = new FakeInputDatabase();
    }

    @Test
    public void WhenSourceAndConfigGiven_ShouldReturnPopulatedGraph()
    {
        Model graph = rdfMapper.mapToGraph(fakeInputDatabase, mapperConfig);
        //Graph should have 2 triples from the given input and configs.
        long result = graph.size();

        assertThat(result, is(equalTo(Long.valueOf(2))));
    }

    @Test
    public void WhenNoInputSourceGiven_ThenThrowException() {
        String expected = "Cannot retrieve source data from null input source";
        exceptionRule.expect(MapperException.class);
        exceptionRule.expectMessage(expected);
        rdfMapper.mapToGraph(null, mapperConfig);
    }

    @Test
    public void WhenNoConfigMapsGiven_ThenThrowException() {
        String expected = "Cannot map source from null config maps.";
        exceptionRule.expect(MapperException.class);
        exceptionRule.expectMessage(expected);
        rdfMapper.mapToGraph(fakeInputDatabase, null);
    }
}
