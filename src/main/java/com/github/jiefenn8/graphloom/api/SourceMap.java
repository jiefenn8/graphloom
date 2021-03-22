/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import com.github.jiefenn8.graphloom.api.inputsource.Entity;
import com.github.jiefenn8.graphloom.util.UniqueId;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * This interface defines the base methods that provides the information
 * needed to locate and retrieve the desired data from the data-source.
 */
public interface SourceMap extends UniqueId {

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
