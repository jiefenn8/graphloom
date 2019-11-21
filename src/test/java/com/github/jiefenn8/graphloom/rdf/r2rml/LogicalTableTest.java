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

import com.github.jiefenn8.graphloom.api.EntityRecord;
import com.github.jiefenn8.graphloom.api.InputSource;
import com.github.jiefenn8.graphloom.api.SourceConfig;
import com.github.jiefenn8.graphloom.api.SourceMap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogicalTableTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    private LogicalTable logicalTable;
    @Mock private SourceConfig mockSourceConfig;
    @Mock private InputSource mockInputSource;

    @Test
    public void GivenInputSource_WhenLoadInputSource_ThenReturnSourceMap(){
        logicalTable = new LogicalTable(mockSourceConfig);
        SourceMap result = logicalTable.loadInputSource(mockInputSource);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenBatchId_WhenGetEntityRecord_ThenReturnEntityRecord(){
        when(mockInputSource.getEntityRecord(any(SourceConfig.class), anyInt())).thenReturn(mock(EntityRecord.class));
        logicalTable = new LogicalTable(mockSourceConfig);
        logicalTable.loadInputSource(mockInputSource);
        EntityRecord result = logicalTable.getEntityRecord(0);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenNullSourceConfig_WhenCreateInstance_ThenThrowException(){
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("Payload must not be null.");
        logicalTable = new LogicalTable(null);
    }
}