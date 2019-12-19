/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import java.util.Set;

/**
 * This interface defines the base methods that manages the mapping
 * of data to their graph terms sharing the same entity.
 */
public interface EntityMap extends PropertyMap {

    /**
     * Returns the source map after applying input source with the entity
     * map in preparation for the retrieval calls of entity record.
     *
     * @param s input source to apply to mapping configs
     * @return the source map ready to be queried and iterated on
     */
    SourceMap applySource(InputSource s);

    /**
     * Returns the unique id name that identify this entity map.
     *
     * @return unique id name of this entity map
     */
    String getIdName();

    /**
     * Returns all relation maps that this entity map has.
     *
     * @return the set containing all relation maps in this entity map
     */
    Set<RelationMap> listRelationMaps();

    /**
     * Returns the node map associated with the given relation map.
     *
     * @param m relation mapping to be searched in entity map for its
     *          paired node map
     * @return the node map found with the given relation map
     */
    NodeMap getNodeMapWithRelation(RelationMap m);

    /**
     * Returns true if this entity mapping has any relation map and
     * node map pairs'.
     *
     * @return true if there are any pair, otherwise false
     */
    boolean hasRelationNodeMaps();

}
