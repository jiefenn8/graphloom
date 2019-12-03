/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import java.util.List;
import java.util.Map;

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
     * Returns all entity map that exists in the configuration mappings.
     *
     * @return the list containing all entity map associated
     *         with the mapping
     */
    List<EntityMap> listEntityMaps();
}