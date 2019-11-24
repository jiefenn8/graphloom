/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import org.apache.jena.rdf.model.Model;

/**
 * This interface defines the base methods that manages the mapping
 * of a input source using provided mapping configurations.
 */
public interface GraphMapper {

    /**
     * Returns the resulting mapping of input source applied to the config
     * mappings given. Returns an empty model if the given
     * input source or configuration mappings was not sufficient enough
     * to generate any semantic terms.
     *
     * @param s input source containing the data to map over to graph
     * @param c config maps to manage the mapping of data
     * @return the model containing the mapped source as a graph model
     */
    Model mapToGraph(InputSource s, ConfigMaps c);
}
