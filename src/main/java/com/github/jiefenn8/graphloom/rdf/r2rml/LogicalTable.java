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

import com.github.jiefenn8.graphloom.api.*;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.Validate;

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
