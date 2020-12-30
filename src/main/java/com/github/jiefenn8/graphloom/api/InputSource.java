/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

/**
 * This interface defines the base methods to retrieve data of entities as
 * a entity record from a data source.
 */
public interface InputSource {

    /**
     * Returns a collection of record for an entity containing data
     * representing it properties and their values. If the query result
     * is a large data-set, the InputSource may split the result and return
     * several batches of the result. The retrieval of specific batch can be
     * controlled by specifying the seq number.
     * E.g. 2000 items, fetch size of 500 = 4 batches; batch 2 will be the 2nd
     * collection of data.
     *
     * @param sourceConfig the config containing the query for this entity
     * @param batchId      the batch with the given id to retrieve
     * @return the collection of records as EntityRecord. Else return empty
     * entity map if there is no more records to return
     */
    EntityRecord getEntityRecord(SourceConfig sourceConfig, int batchId);

    /**
     * Returns the number of batches to expect for a source config.
     *
     * @param sourceConfig the config containing the query for this entity
     * @return the number of batches possible
     */
    int calculateNumOfBatches(SourceConfig sourceConfig);
}
