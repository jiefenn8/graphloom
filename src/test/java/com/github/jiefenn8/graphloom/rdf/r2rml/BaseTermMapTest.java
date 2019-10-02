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

import com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermMapType;
import com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermType;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class BaseTermMapTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock Map<String, String> mockRow;
    private BaseTermMap baseTermMap;

    @Test
    @Parameters({"CONSTANT", "TEMPLATE", "COLUMN"})
    public void WhenTermMapTypeGiven_ThenReturnType(TermMapType type){
        TermMapType expected = type;
        baseTermMap = new BaseTermMap(type, "constant", TermType.IRI);
        TermMapType result = baseTermMap.getTermMapType();
        assertThat(result, is(equalTo(expected)));
    }

    //Column tests

    @Test
    public void WhenTermTypeIRIGiven_ThenReturnConstantTermAsResource(){
        RDFNode expected = ResourceFactory.createResource("constant");
        baseTermMap = new BaseTermMap(TermMapType.CONSTANT, expected);
        boolean result = baseTermMap.generateConstantTerm().isResource();
        assertThat(result, is(true));
    }

    @Test
    public void WhenTermTypeLiteralGiven_ThenReturnConstantTermAsLiteral(){
        RDFNode expected = ResourceFactory.createStringLiteral("constant");
        baseTermMap = new BaseTermMap(TermMapType.CONSTANT, expected);
        boolean result = baseTermMap.generateConstantTerm().isLiteral();
        assertThat(result, is(true));
    }

    //Template test

    @Test
    public void WhenTermTypeIRIGiven_ThenReturnTemplateTermAsResource(){
        String template = "Template/{Col_1_Type}";
        baseTermMap = new BaseTermMap(TermMapType.TEMPLATE, template, TermType.IRI);
        when(mockRow.get("Col_1_Type")).thenReturn("Col_1_Val");
        boolean result = baseTermMap.generateTemplateTerm(mockRow).isResource();
        assertThat(result, is(true));
    }

    @Test
    public void WhenTermTypeLiteralGiven_ThenReturnTemplateTermAsLiteral(){
        String template = "Template/{Col_1_Type}";
        baseTermMap = new BaseTermMap(TermMapType.TEMPLATE, template, TermType.LITERAL);
        when(mockRow.get("Col_1_Type")).thenReturn("Col_1_Val");
        boolean result = baseTermMap.generateTemplateTerm(mockRow).isLiteral();
        assertThat(result, is(true));
    }

    //Column

    @Test
    public void WhenTermTypeIRIGiven_ThenReturnColumnTermAsResource(){
        String column = "Col_1_Type";
        baseTermMap = new BaseTermMap(TermMapType.COLUMN, column, TermType.IRI, false);
        when(mockRow.get("Col_1_Type")).thenReturn("Col_1_Val");
        boolean result = baseTermMap.generateColumnTerm(mockRow).isResource();
        assertThat(result, is(true));
    }

    @Test
    public void WhenTermTypeLiteralGiven_ThenReturnColumnTermAsLiteral(){
        String column = "Col_1_Type";
        baseTermMap = new BaseTermMap(TermMapType.COLUMN, column, TermType.LITERAL, false);
        when(mockRow.get("Col_1_Type")).thenReturn("Col_1_Val");
        boolean result = baseTermMap.generateColumnTerm(mockRow).isLiteral();
        assertThat(result, is(true));
    }

    //Other

    @Test
    @Parameters({"IRI", "BLANK", "LITERAL"})
    public void WhenEntityRecordGiven_ThenReturnNonNullTemplateTerm(TermType type) {
        String template = "Template/{Col_1_Type}";
        baseTermMap = new BaseTermMap(TermMapType.TEMPLATE, template, type);
        when(mockRow.get("Col_1_Type")).thenReturn("Col_1_Val");
        RDFNode result = baseTermMap.generateTemplateTerm(mockRow);
        assertThat(result, notNullValue());
    }

    @Test
    @Parameters({"IRI", "BLANK", "LITERAL"})
    public void WhenEntityRecordGiven_ThenReturnNonNullConstantTerm(TermType type) {
        RDFNode constant = mock(RDFNode.class);
        baseTermMap = new BaseTermMap(TermMapType.CONSTANT, constant);
        RDFNode result = baseTermMap.generateConstantTerm();
        assertThat(result, notNullValue());
    }

    @Test
    @Parameters({"IRI", "BLANK", "LITERAL"})
    public void WhenEntityRecordGiven_ThenReturnNonNullColumnTerm(TermType type) {
        String column = "Col_1_Type";
        baseTermMap = new BaseTermMap(TermMapType.COLUMN, column, type, false);
        when(mockRow.get("Col_1_Type")).thenReturn("Col_1_Val");
        RDFNode result = baseTermMap.generateColumnTerm(mockRow);
        assertThat(result, notNullValue());
    }

}
