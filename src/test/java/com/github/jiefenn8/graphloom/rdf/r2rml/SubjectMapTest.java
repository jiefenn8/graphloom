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

import com.github.jiefenn8.graphloom.common.HashRecord;
import com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermType;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubjectMapTest {

    @Mock private HashRecord mockRecord;
    private SubjectMap subjectMap;

    @Before
    public void setUp() throws Exception {
        when(mockRecord.getPropertyValue("Col_1_Type")).thenReturn("Col_1_Val");
    }

    @Test
    public void WhenConstantTermMapTypeGiven_ThenReturnTermAsResource() {
        Resource rdfNode = ResourceFactory.createResource("constant");
        subjectMap = R2RMLFactory.createConstSubjectMap(rdfNode);
        boolean result = subjectMap.generateEntityTerm(mockRecord).isResource();
        assertThat(result, is(true));
    }

    @Test
    public void WhenTemplateTermMapTypeGiven_ThenReturnTermAsResource() {
        subjectMap = R2RMLFactory.createTmplSubjectMap("Template/{Col_1_Type}");
        boolean result = subjectMap.generateEntityTerm(mockRecord).isResource();
        assertThat(result, is(true));
    }

    @Test
    public void WhenColumnTermMapTypeGiven_ThenReturnTermAsResource() {
        subjectMap = R2RMLFactory.createColSubjectMap("Col_1_Type", TermType.IRI);
        boolean result = subjectMap.generateEntityTerm(mockRecord).isResource();
        assertThat(result, is(true));
    }

    @Test
    public void WhenClassGiven_ThenReturnNonEmptyList() {
        subjectMap = R2RMLFactory.createTmplSubjectMap("Template/{Col_1_Type}");
        Resource expected = ResourceFactory.createResource("resource");
        subjectMap.addClass(expected);
        boolean result = subjectMap.listEntityClasses().isEmpty();
        assertThat(result, is(false));
    }

    @Test
    public void WhenNoClassGiven_ThenReturnEmptyList() {
        subjectMap = R2RMLFactory.createTmplSubjectMap("Template/{Col_1_Type}");
        boolean result = subjectMap.listEntityClasses().isEmpty();
        assertThat(result, is(true));
    }
}