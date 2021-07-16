/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.api.inputsource;

/**
 * This interface defines the common base methods in handling an entity and its
 * properties. Implementations of this interface should use the adapter pattern
 * to wrap existing data source implementation with their data retrieval
 * methods; And expose them through this API without the need of additional
 * data store in memory.
 */
public interface Entity {

    /**
     * Returns the value of an entity property. If the property or
     * value does not exists; Returns null.
     *
     * @param name the name of the entity property
     * @return the value of the given property name, otherwise null
     */
    String getPropertyValue(String name);
}

