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

public class InputSourceConfig implements SourceConfig {

    private PayloadType payloadType;
    private String payload;
    private String iteratorDef;
    private Map<String, String> properties = new HashMap<>();

    protected InputSourceConfig(String payload, PayloadType pt, String iteratorDef){
        this.payload = Preconditions.checkNotNull(payload, "Payload must not be null.");
        this.payloadType = Preconditions.checkNotNull(pt, "Payload type must not be null.");
        this.iteratorDef = Preconditions.checkNotNull(iteratorDef, "Iterator definition must not be null.");
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

    /**
     * Sets a custom property to this config with given property name
     * and value. Returns the value of the previous value of the
     * associated key or null if there was no value set for this property.
     *
     * @param property the name of the property to add to this config
     * @param value the value to associate to the given property
     * @return the previous value that was associated with the given property
     */
    public String setProperty(String property, String value){
        Preconditions.checkNotNull(property);
        return properties.put(property, value);
    }

    @Override
    public String getProperty(String propertyName) {
        return properties.get(propertyName);
    }
}
