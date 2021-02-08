/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import com.github.jiefenn8.graphloom.api.inputsource.Entity;
import org.apache.jena.rdf.model.RDFNode;

/**
 * This interface defines the base method that manages the mapping of any
 * nodes to their graph node term.
 */
public interface NodeMap {

    /**
     * Generates a node term with this map using the given entity to
     * retrieve any data needed.
     *
     * @param entity containing any data needed to generate term
     * @return the node term generated by this node map
     */
    RDFNode generateNodeTerm(Entity entity);
}
