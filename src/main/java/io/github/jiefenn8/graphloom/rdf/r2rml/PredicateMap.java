/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import com.google.gson.GsonBuilder;
import io.github.jiefenn8.graphloom.api.RelationMap;
import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import io.github.jiefenn8.graphloom.util.GsonHelper;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import java.util.UUID;

/**
 * Implementation of R2RML PredicateMap with {@link RelationMap} interface.
 * This term map will return either a rr:IRI for its main term.
 */
public class PredicateMap extends AbstractTermMap implements RelationMap {

    private final UUID uuid = UUID.randomUUID();

    /**
     * Constructs an PredicateMap with the specified TermMap Builder containing the
     * * data to initialise an immutable instance.
     *
     * @param builder the TermMap Builder to builder instance from
     */
    protected PredicateMap(Builder builder) {
        super(builder);
    }

    @Override
    public Property generateRelationTerm(Entity entity) {
        Resource term = generateRDFTerm(entity).asResource();
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

    @Override
    protected RDFNode handleDefaultGeneration(String term) {
        return asRDFTerm(term, TermType.IRI);
    }

    public static class Builder extends AbstractBuilder<PredicateMap> {

        public Builder(RDFNode baseValue, ValuedType valuedType) {
            super(baseValue, valuedType);
        }

        @Override
        public PredicateMap build() {
            return new PredicateMap(this);
        }
    }
}
