/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.EntityChild;
import com.github.jiefenn8.graphloom.api.EntityMap;
import com.github.jiefenn8.graphloom.api.Record;
import com.github.jiefenn8.graphloom.api.RelationMap;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Implementation of R2RML PredicateMap with {@link RelationMap} interface.
 * This term map will return either a rr:IRI for its main term.
 */
public class PredicateMap implements RelationMap, EntityChild {

    private EntityMap parent;
    private TermMap termMap;

    protected PredicateMap(TermMap m) {
        this.termMap = checkNotNull(m, "Term map must not be null.");
    }

    @Override
    public Property generateRelationTerm(Record r) {
        Resource term = termMap.generateRDFTerm(r).asResource();
        return ResourceFactory.createProperty(term.getURI());
    }

    protected PredicateMap withParentMap(EntityMap m) {
        parent = m;
        return this;
    }

    @Override
    public EntityMap getParentMap() {
        return parent;
    }
}
