/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.*;
import com.google.common.base.Preconditions;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML LogicalTable with {@link SourceMap} interface.
 */
public class LogicalTable implements SourceMap {

    private SourceConfig sourceConfig;
    private InputSource inputSource;

    protected LogicalTable(SourceConfig sourceConfig) {
        this.sourceConfig = checkNotNull(sourceConfig, "Payload must not be null.");
    }

    @Override
    public EntityRecord getEntityRecord(int batchId) {
        return inputSource.getEntityRecord(sourceConfig, batchId);
    }

    @Override
    public SourceMap loadInputSource(InputSource s) {
        inputSource = s;
        return this;
    }

    @Override
    public void forEachEntityRecord(Consumer<Record> action) {
        Preconditions.checkNotNull(action);
        int totalBatch = inputSource.calculateNumOfBatches(sourceConfig);
        for (int batchId = 0; batchId < totalBatch; batchId++) {
            EntityRecord entityRecord = getEntityRecord(batchId);
            for (Record record : entityRecord) {
                action.accept(record);
            }
        }
    }
}
