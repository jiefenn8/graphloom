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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.apache.jena.ext.com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML LogicalTable with {@link SourceMap} interface.
 */
public class LogicalTable implements SourceMap, SourceConfig {

    private final String payload;
    private final PayloadType payloadType;
    private final String iteratorDef;
    private final Map<String, String> exProperty = new HashMap<>();
    private InputSource inputSource;

    protected LogicalTable(String payload, PayloadType payloadType) {
        this.payload = checkNotNull(payload);
        this.payloadType = checkNotNull(payloadType);
        iteratorDef = "";
    }

    @Override
    public EntityRecord getEntityRecord(int batchId) {
        return inputSource.getEntityRecord(this, batchId);
    }

    @Override
    public PayloadType getPayloadType() {
        return payloadType;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public String getIteratorDef() {
        return iteratorDef;
    }

    @Override
    public String getProperty(String propertyName) {
        return exProperty.get(propertyName);
    }

    protected void setProperty(String propertyName, String propertyValue) {
        exProperty.put(propertyName, propertyValue);
    }

    @Override
    public SourceMap loadInputSource(InputSource source) {
        inputSource = checkNotNull(source);
        return this;
    }

    @Override
    public void forEachEntityRecord(Consumer<Record> action) {
        int totalBatch = inputSource.calculateNumOfBatches(this);
        for (int batchId = 0; batchId < totalBatch; batchId++) {
            EntityRecord entityRecord = getEntityRecord(batchId);
            for (Record record : entityRecord) {
                checkNotNull(action).accept(record);
            }
        }
    }

    protected enum DbPayloadType implements PayloadType {
        QUERY, TABLENAME
    }
}
