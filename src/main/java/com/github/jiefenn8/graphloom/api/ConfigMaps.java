/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import java.util.Map;
import java.util.Set;

/**
 * This interface defines the base methods to express the rules
 * from many configurations that will map data-source to a graph model.
 */
public interface ConfigMaps extends Iterable<EntityMap> {

    /**
     * Returns map of all namespace prefix and URI that was used in the
     * mapping document.
     *
     * @return the map containing all namespace prefixes and their URIs
     */
    Map<String, String> getNamespaceMap();

    /**
     * Returns all unique entity map that exists in the configuration
     * mappings.
     *
     * @return the set containing all unique entity maps
     */
    Set<EntityMap> getEntityMaps();
}