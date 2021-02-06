/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import com.github.jiefenn8.graphloom.api.inputsource.Entity;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * This interface defines the base methods that provides the information
 * needed to locate and retrieve the desired data from the data-source.
 */
public interface SourceMap {

    /**
     * Returns this source map after applying it on the given input source.
     *
     * @param inputSource the source containing the data to map over to graph
     * @return the source map ready to be queried and iterated on
     */
    SourceMap loadInputSource(InputSource inputSource);

    /**
     * This call will consume each record given from the input source after
     * querying and apply the consumer action on each of them. This method
     * is used when it is desired to iterate through all the available entity
     * records from input source with several batches.
     *
     * @param action the action to perform on each record
     */
    @Deprecated
    void forEachEntityRecord(Consumer<Record> action);

    /**
     * Returns an entity record result based on the batch id; containing
     * records of an entity from applied source map.
     *
     * @return the collection of records for an entity
     */
    @Deprecated
    EntityRecord getEntityRecord(int batchId);

    /**
     * Returns the {@link EntityReference} associated with this instance. Every
     * instance should have a reference containing the required information to
     * locate the specific data from the source to map into an entity.
     *
     * @return the reference containing information to locate entity data
     */
    EntityReference getEntityReference();

    /**
     * Iterates through the received collection of entities from source and
     * apply any defined actions to each entity.
     *
     * @param inputSource containing the data source to query
     * @param action      to apply to each entity found
     */
    default void forEachEntity(InputSource inputSource, Consumer<Entity> action) {
        Objects.requireNonNull(action);
        inputSource.executeEntityQuery(getEntityReference(), (r) -> {
            while (r.hasNext()) {
                action.accept(r.nextEntity());
            }
        });
    }
}
