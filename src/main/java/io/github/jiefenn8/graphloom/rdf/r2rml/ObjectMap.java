/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import com.google.gson.GsonBuilder;
import io.github.jiefenn8.graphloom.api.NodeMap;
import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import io.github.jiefenn8.graphloom.util.GsonHelper;
import org.apache.jena.rdf.model.RDFNode;

import java.util.UUID;

/**
 * Implementation of R2RML ObjectMap with {@link NodeMap} interface.
 * This term map will return either a rr:IRI, rr:BlankNode or rr:Literal for its main term.
 */
public class ObjectMap extends AbstractTermMap implements NodeMap {

    private final UUID uuid = UUID.randomUUID();

    /**
     * Constructs an ObjectMap with the specified TermMap Builder containing the
     * data to initialise an immutable instance.
     *
     * @param builder the TermMap Builder to builder instance from
     */
    protected ObjectMap(Builder builder) {
        super(builder);
    }

    @Override
    public RDFNode generateNodeTerm(Entity entity) {
        return generateRDFTerm(entity);
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
        if (valuedType.equals(ValuedType.COLUMN) || !lang.isEmpty() || dataType != null) {
            return asRDFTerm(term, TermType.LITERAL);
        }
        return asRDFTerm(term, TermType.IRI);
    }

    public static class Builder extends AbstractBuilder<ObjectMap> {

        public Builder(RDFNode baseValue, ValuedType valuedType) {
            super(baseValue, valuedType);
        }

        @Override
        public ObjectMap build() {
            return new ObjectMap(this);
        }
    }
}
