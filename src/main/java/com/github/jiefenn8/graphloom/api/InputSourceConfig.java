/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InputSourceConfig implements SourceConfig {

    private PayloadType payloadType;
    private String payload;
    private String iteratorDef;
    private Map<String, String> properties = new HashMap<>();

    protected InputSourceConfig(String payload, PayloadType pt, String iteratorDef) {
        this.payload = Preconditions.checkNotNull(payload, "Payload must not be null.");
        this.payloadType = Preconditions.checkNotNull(pt, "Payload type must not be null.");
        this.iteratorDef = Preconditions.checkNotNull(iteratorDef, "Iterator definition must not be null.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputSourceConfig that = (InputSourceConfig) o;
        return Objects.equals(payloadType, that.payloadType) &&
                Objects.equals(payload, that.payload) &&
                Objects.equals(iteratorDef, that.iteratorDef) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payloadType, payload, iteratorDef, properties);
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
     * @param value    the value to associate to the given property
     * @return the previous value that was associated with the given property
     */
    public String setProperty(String property, String value) {
        Preconditions.checkNotNull(property);
        return properties.put(property, value);
    }

    @Override
    public String getProperty(String propertyName) {
        return properties.get(propertyName);
    }
}
