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
import com.github.jiefenn8.graphloom.common.LinkedHashEntityRecord;
import com.github.jiefenn8.graphloom.rdf.r2rml.LogicalTable.DbPayloadType;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class LogicalTableTest {

    private final String payload = "PAYLOAD";
    private LogicalTable logicalTable;

    @Test
    @Parameters({"QUERY", "TABLENAME"})
    public void WhenEntityRecordRetrieved_ThenReturnEntityRecord(DbPayloadType payloadType) {
        logicalTable = new LogicalTable(payload, payloadType);
        InputSource mockInputSource = mock(InputSource.class);
        logicalTable.loadInputSource(mockInputSource);
        when(mockInputSource.getEntityRecord(any(SourceConfig.class), anyInt()))
                .thenReturn(mock(LinkedHashEntityRecord.class));

        EntityRecord result = logicalTable.getEntityRecord(0);
        assertThat(result, notNullValue());
    }
}