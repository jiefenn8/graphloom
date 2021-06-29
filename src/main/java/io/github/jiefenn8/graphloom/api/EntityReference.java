/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.api;

/**
 * This interface defines the base methods to manage the handling of
 * payload and its related properties for consumption by the input
 * source.
 */
public interface EntityReference {

    /**
     * Returns the payload type of this class.
     *
     * @return the payload type
     */
    PayloadType getPayloadType();

    /**
     * Returns the payload string that defines query on what Entity
     * result is needed to continue the mapping.
     *
     * @return the payload containing info needed to execute the query
     */
    String getPayload();

    /**
     * Returns a definition that defines how to iterate through each
     * entity in result.
     *
     * @return the value containing the iteration definition
     */
    String getIteratorDef();

    /**
     * Returns the value of a property in this configuration
     *
     * @param propertyName the given property name to retrieve its value
     * @return the value of the given property otherwise null
     */
    String getProperty(String propertyName);

    /**
     * Default types of payload.
     */
    enum DefaultType implements PayloadType {
        UNDEFINED
    }

    /**
     * This interface defines the base methods to manage the type of payload
     * the query or reference can be.
     */
    interface PayloadType {
    }
}
