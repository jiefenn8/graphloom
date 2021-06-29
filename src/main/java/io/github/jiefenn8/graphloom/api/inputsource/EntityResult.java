/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.api.inputsource;

/**
 * This interface defines the common base methods in handling the result
 * containing any relevant entity of a query. Implementations of this interface
 * should use the adapter pattern to wrap existing data source implementation
 * with their data retrieval methods; And expose them through this API without
 * the need of additional data store in memory.
 */
public interface EntityResult {

    /**
     * Returns true if the result iterator has an entity next. If there is no
     * entity next in the iterator; Returns false.
     *
     * @return true if there is an entity next, otherwise false
     */
    boolean hasNext();

    /**
     * Returns the next entity in the result iterator.
     *
     * @return the next entity on the iterator
     */
    Entity nextEntity();
}
