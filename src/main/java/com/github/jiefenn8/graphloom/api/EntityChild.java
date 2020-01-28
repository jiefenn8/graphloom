/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

/**
 * This interface defines the base methods to manage any property
 * of that is associated to an entity map; such that any instance of
 * this implementation is owned by zero or one entity map.
 */
public interface EntityChild {

    /**
     * Returns the entity map that this mapping is associated with.
     *
     * @return the entity map that owns this map
     */
    EntityMap getEntityMap();
}
