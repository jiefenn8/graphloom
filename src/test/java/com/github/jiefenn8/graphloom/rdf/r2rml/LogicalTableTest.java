/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
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

/**
 * Unit test class for {@link LogicalTable}.
 */
@RunWith(MockitoJUnitRunner.class)
public class LogicalTableTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    private LogicalTable logicalTable;
    @Mock private SourceConfig mockSourceConfig;
    @Mock private InputSource mockInputSource;

    @Test
    public void GivenInputSource_WhenLoadInputSource_ThenReturnSourceMap() {
        logicalTable = new LogicalTable.Builder(mockSourceConfig).build();
        SourceMap result = logicalTable.loadInputSource(mockInputSource);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenBatchId_WhenGetEntityRecord_ThenReturnEntityRecord() {
        when(mockInputSource.getEntityRecord(any(), anyInt())).thenReturn(mock(EntityRecord.class));
        logicalTable = new LogicalTable.Builder(mockSourceConfig).build();
        logicalTable.loadInputSource(mockInputSource);
        EntityRecord result = logicalTable.getEntityRecord(0);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenNullSourceConfig_WhenBuildInstance_ThenThrowException() {
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("Payload must not be null.");
        logicalTable = new LogicalTable.Builder((SourceConfig) null).build();
    }
}