/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.EntityChild;
import com.github.jiefenn8.graphloom.api.EntityMap;
import com.github.jiefenn8.graphloom.api.RelationMap;
import com.github.jiefenn8.graphloom.api.inputsource.Entity;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Implementation of R2RML PredicateMap with {@link RelationMap} interface.
 * This term map will return either a rr:IRI for its main term.
 */
public class PredicateMap implements RelationMap, EntityChild {

    private final TermMap termMap;
    private EntityMap parent;

    /**
     * Constructs an PredicateMap with the specified term map that is either
     * a constant, template or a column type.
     *
     * @param termMap the term map that this instance will behave as
     */
    protected PredicateMap(TermMap termMap) {
        this.termMap = checkNotNull(termMap, "Term map must not be null.");
    }

    @Override
    public Property generateRelationTerm(Entity entity) {
        Resource term = termMap.generateRDFTerm(entity).asResource();
        return ResourceFactory.createProperty(term.getURI());
    }

    /**
     * Adds association to an triples map that this predicate map belongs to.
     *
     * @param entityMap the triples map to associate with
     * @return this builder for fluent method chaining
     */
    protected PredicateMap withParentMap(EntityMap entityMap) {
        parent = entityMap;
        return this;
    }

    @Override
    public EntityMap getEntityMap() {
        return parent;
    }
}
