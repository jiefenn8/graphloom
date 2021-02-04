/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import com.github.jiefenn8.graphloom.api.inputsource.EntityResult;

import java.util.function.Consumer;

/**
 * This interface defines the base methods in retrieving relevant data grouped
 * through a defined entity; usually by name reference or a query.
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
    EntityRecord getEntityRecord(EntityReference sourceConfig, int batchId);

    /**
     * Returns the number of batches to expect for a query.
     *
     * @param sourceConfig the config containing the query for this entity
     * @return the number of batches possible
     */
    int calculateNumOfBatches(EntityReference sourceConfig);

    /**
     * Execute the reference or query in {@link EntityReference} to obtain
     * relevant data as {@link EntityResult} and apply any action to it.
     *
     * @param entityRef the reference or query to retrieve relevant data
     * @param action    the action to apply to the results
     */
    void executeEntityQuery(EntityReference entityRef, Consumer<EntityResult> action);
}
