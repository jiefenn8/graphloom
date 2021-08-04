/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.api;

import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import io.github.jiefenn8.graphloom.util.UniqueId;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

/**
 * This interface defines the base methods that manages the mapping
 * of data to their graph terms sharing the same entity.
 */
public interface EntityMap extends UniqueId {

    /**
     * Returns the unique id name that identify this instance.
     *
     * @return unique id name of this entity map
     */
    String getIdName();

    /**
     * Returns the source map that has the data source and reference required to
     * query and map the required data into a graph.
     *
     * @return the source map associated with this entity map
     */
    SourceMap getSourceMap();

    /**
     * Returns generated entity term of this instance. If the term cannot be
     * generated, returns null.
     *
     * @param entity containing the entity data to generate the term
     * @return the URI term generated for this entity, otherwise null
     */
    Resource generateEntityTerm(Entity entity);

    /**
     * Returns a new model with the given entity term and with any generated
     * class terms associated with the entity.
     *
     * @param term of the entity that represent this entity map
     * @return the model containing the entity and class terms
     */
    Model generateClassTerms(Resource term);

    /**
     * Returns true if this entity mapping has any relation map and
     * node map pairs.
     *
     * @return true if there are any pair, otherwise false
     */
    boolean hasNodeMapPairs();

    /**
     * Returns a model containing RDF triples of all entity properties.
     *
     * @param term   of the entity that represent this entity map
     * @param entity containing the entity data to generate the term
     * @return model containing RDF triples related to an entity
     */
    Model generateNodeTerms(Resource term, Entity entity);

    /**
     * Returns a model containing RDF triples of all entity properties that
     * reference to an existing entity in the {@link ConfigMaps} that this
     * instance belongs to.
     *
     * @param term   of the entity that represent this entity map
     * @param source containing the data source to query data
     * @return model containing RDF triples related to an entity
     */
    Model generateRefNodeTerms(Resource term, InputSource source);
}
