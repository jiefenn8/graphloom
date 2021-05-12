/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.inputsource.Entity;
import com.github.jiefenn8.graphloom.util.GsonHelper;
import com.google.gson.GsonBuilder;
import org.apache.jena.rdf.model.RDFNode;

import java.util.Objects;

/**
 * This interface defines the base methods that manages the mapping of any
 * source entity to their respective rdf term through the use of a constant
 * term.
 */
@Deprecated
public class ConstTermMap implements TermMap {

    private final RDFNode constTerm;

    /**
     * Constructs a ConstTermMap with the specified constant value to use
     * as the term.
     *
     * @param node the constant value to use as term
     */
    protected ConstTermMap(RDFNode node) {
        constTerm = Objects.requireNonNull(node, "Constant term must not be null.");
    }

    @Override
    public RDFNode generateRDFTerm(Entity entity) {
        return constTerm;
    }

    @Override
    public String toString() {
        return GsonHelper.loadTypeAdapters(new GsonBuilder())
                .create()
                .toJson(this);
    }
}
