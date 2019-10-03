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

import com.github.jiefenn8.graphloom.rdf.parser.R2RMLParser;
import com.github.jiefenn8.graphloom.rdf.r2rml.R2RMLMap;
import org.apache.jena.shared.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class R2RMLParserTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    private R2RMLParser r2rmlParser;

    @Before
    public void setUp(){
        r2rmlParser = new R2RMLParser();
    }

    @Test
    public void WhenValidFileGiven_ThenReturnR2RMLMap() {
        R2RMLMap result = r2rmlParser.parse("valid_r2rml.ttl", null);

        assertThat(result, notNullValue());
    }

    @Test
    public void WhenInvalidFilePathGiven_ThenThrowException() {
        String invalid_path = "invalid_filepath.ttl";

        exceptionRule.expect(NotFoundException.class);
        r2rmlParser.parse(invalid_path);
    }
}
