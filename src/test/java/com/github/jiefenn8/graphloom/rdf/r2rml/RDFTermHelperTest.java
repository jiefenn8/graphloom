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

import com.github.jiefenn8.graphloom.exceptions.MapperException;
import com.google.common.collect.ImmutableList;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.List;

import static com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class RDFTermHelperTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    private final String value = "TERM_VALUE";

    public List<TermType> termTypeParameters(){
        return ImmutableList.of(TermType.IRI, TermType.LITERAL);
    }

    @Test
    @Parameters(method = "termTypeParameters")
    public void GivenNullValue_WhenCreateRDFTerm_ThenThrowException(TermType t){
        expectedException.expect(NullPointerException.class);
        RDFTermHelper.asRDFTerm(null, t);
    }

    @Test
    public void GivenUndefinedTermType_WhenCreateRDFTerm_ThenThrowException(){
        expectedException.expect(MapperException.class);
        expectedException.expectMessage("Term type is UNDEFINED.");
        RDFTermHelper.asRDFTerm(value, TermType.UNDEFINED);
    }

    @Test
    public void GivenNullTermType_WhenCreateRDFTerm_ThenThrowException(){
        expectedException.expect(NullPointerException.class);
        RDFTermHelper.asRDFTerm(value, null);
    }

    @Test
    public void GivenValueWithIRIType_WhenCreateRDFTerm_ThenReturnResource(){
        RDFNode term = RDFTermHelper.asRDFTerm(value, TermType.IRI);
        boolean result = term.isURIResource();
        assertThat(result, is(true));
    }

    @Test
    public void GivenValueWithLiteralType_WhenCreateRDFTerm_ThenReturnLiteral(){
        RDFNode term = RDFTermHelper.asRDFTerm(value, TermType.LITERAL);
        boolean result = term.isLiteral();
        assertThat(result, is(true));
    }

    @Test
    public void GivenValueWithBlankType_WhenCreateRDFTerm_ThenReturnBlank(){
        RDFNode term = RDFTermHelper.asRDFTerm(value, TermType.BLANK);
        boolean result = term.isAnon();
        assertThat(result, is(true));
    }
}