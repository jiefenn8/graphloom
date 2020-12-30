/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.EntityChild;
import com.github.jiefenn8.graphloom.api.EntityMap;
import com.github.jiefenn8.graphloom.api.NodeMap;
import com.github.jiefenn8.graphloom.api.Record;
import org.apache.jena.rdf.model.RDFNode;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML ObjectMap with {@link NodeMap} interface.
 * This term map will return either a rr:IRI, rr:BlankNode or rr:Literal for its main term.
 */
public class ObjectMap implements NodeMap, EntityChild {

    private EntityMap parent;
    private final TermMap termMap;

    /**
     * Constructs an ObjectMap with the specified term map that is either
     * a constant, template or a column type.
     *
     * @param termMap the term map that this instance will behave as
     */
    protected ObjectMap(TermMap termMap) {
        this.termMap = checkNotNull(termMap, "Term map must not be null.");
    }

    /**
     * Adds association to an triples map that this object map belongs to.
     *
     * @param entityMap the triples map to associate with
     * @return this builder for fluent method chaining
     */
    protected ObjectMap withParentMap(EntityMap entityMap) {
        parent = entityMap;
        return this;
    }

    @Override
    public EntityMap getEntityMap() {
        return parent;
    }

    @Override
    public RDFNode generateNodeTerm(Record record) {
        return termMap.generateRDFTerm(record);
    }
}
