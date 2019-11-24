/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import java.util.function.Consumer;

/**
 * This interface defines the base methods that provides the information
 * needed to locate and retrieve the desired data from the data-source.
 */
public interface SourceMap {

    /**
     * Returns this source map after applying it on the given input source.
     *
     * @param s input source containing the data to map over to graph
     * @return the source map ready to be queried and iterated on
     */
    SourceMap loadInputSource(InputSource s);

    /**
     * This call will consume each record given from the input source after
     * querying and apply the consumer action on each of them. This method
     * is used when it is desired to iterate through all the available entity
     * records from input source with several batches.
     *
     * @param action the action to perform on each record
     */
    void forEachEntityRecord(Consumer<Record> action);

    /**
     * Returns an entity record result based on the batch id; containing
     * records of an entity from applied source map.
     *
     * @return the collection of records for an entity
     */
    EntityRecord getEntityRecord(int batchId);
}
