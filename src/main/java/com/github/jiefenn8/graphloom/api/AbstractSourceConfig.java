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

package com.github.jiefenn8.graphloom.api;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSourceConfig implements SourceConfig {

    private PayloadType payloadType;
    private String payload;
    private String iteratorDef;
    private Map<String, String> properties = new HashMap<>();

    protected AbstractSourceConfig(String payload, PayloadType pt, String iteratorDef){
        this.payload = Preconditions.checkNotNull(payload);
        this.payloadType = Preconditions.checkNotNull(pt);
        this.iteratorDef = Preconditions.checkNotNull(iteratorDef);
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

    public void setProperty(String property, String value){
        properties.put(property, value);
    }

    @Override
    public String getProperty(String propertyName) {
        return properties.get(propertyName);
    }
}
