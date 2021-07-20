/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.api.inputsource;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link EntityReference} interface.
 */
public class BaseEntityReference implements EntityReference {

    private final PayloadType payloadType;
    private final String payload;
    private final String iteratorDef;
    private final Map<String, String> properties = new HashMap<>();

    /**
     * Constructs an InputSourceConfig with the specified payload, type
     * and iterator definition.
     *
     * @param payload     the payload query to use
     * @param payloadType the type of payload given
     * @param iteratorDef the definition to iterate each row of results
     */
    protected BaseEntityReference(String payload, PayloadType payloadType, String iteratorDef) {
        this.payload = Objects.requireNonNull(payload, "Payload must not be null.");
        this.payloadType = Objects.requireNonNull(payloadType, "Payload type must not be null.");
        this.iteratorDef = Objects.requireNonNull(iteratorDef, "Iterator definition must not be null.");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseEntityReference that = (BaseEntityReference) obj;
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
        Objects.requireNonNull(property);
        return properties.put(property, value);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public String getProperty(String propertyName) {
        return properties.get(propertyName);
    }
}
