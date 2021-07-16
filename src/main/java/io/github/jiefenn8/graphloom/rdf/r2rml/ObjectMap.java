/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import com.google.gson.GsonBuilder;
import io.github.jiefenn8.graphloom.api.EntityMap;
import io.github.jiefenn8.graphloom.api.NodeMap;
import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import io.github.jiefenn8.graphloom.util.GsonHelper;
import org.apache.jena.rdf.model.RDFNode;

import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of R2RML ObjectMap with {@link NodeMap} interface.
 * This term map will return either a rr:IRI, rr:BlankNode or rr:Literal for its main term.
 */
public class ObjectMap implements NodeMap {

    private final UUID uuid = UUID.randomUUID();
    private final TermMap termMap;

    /**
     * Constructs an ObjectMap with the specified term map that is either
     * a constant, template or a column type.
     *
     * @param termMap the term map that this instance will behave as
     */
    protected ObjectMap(TermMap termMap) {
        this.termMap = Objects.requireNonNull(termMap, "Term map must not be null.");
    }

    @Override
    public RDFNode generateNodeTerm(Entity entity) {
        return termMap.generateRDFTerm(entity);
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
