/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.api;

import io.github.jiefenn8.graphloom.api.inputsource.EntityResult;

import java.util.function.Consumer;

/**
 * This interface defines the base methods in retrieving relevant data grouped
 * through a defined entity; usually by name reference or a query.
 */
public interface InputSource {

    /**
     * Execute the reference or query in {@link EntityReference} to obtain
     * relevant data as {@link EntityResult} and apply any action to it.
     *
     * @param entityRef the reference or query to retrieve relevant data
     * @param action    the action to apply to the results
     */
    void executeEntityQuery(EntityReference entityRef, Consumer<EntityResult> action);
}
