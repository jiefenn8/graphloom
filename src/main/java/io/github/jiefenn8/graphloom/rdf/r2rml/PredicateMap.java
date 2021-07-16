/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import com.google.gson.GsonBuilder;
import io.github.jiefenn8.graphloom.api.EntityMap;
import io.github.jiefenn8.graphloom.api.RelationMap;
import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import io.github.jiefenn8.graphloom.util.GsonHelper;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of R2RML PredicateMap with {@link RelationMap} interface.
 * This term map will return either a rr:IRI for its main term.
 */
public class PredicateMap implements RelationMap {

    private final UUID uuid = UUID.randomUUID();
    private final TermMap termMap;
    private EntityMap parent;

    /**
     * Constructs an PredicateMap with the specified term map that is either
     * a constant, template or a column type.
     *
     * @param termMap the term map that this instance will behave as
     */
    protected PredicateMap(TermMap termMap) {
        this.termMap = Objects.requireNonNull(termMap, "Term map must not be null.");
    }

    @Override
    public Property generateRelationTerm(Entity entity) {
        Resource term = termMap.generateRDFTerm(entity).asResource();
        return ResourceFactory.createProperty(term.getURI());
    }

    @Override
    public String toString() {
        return GsonHelper.loadTypeAdapters(new GsonBuilder())
                .create()
                .toJson(this);
    }

    @Override
    public String getUniqueId() {
        return uuid.toString();
    }
}
