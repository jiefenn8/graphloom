/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.inputsource.Entity;
import com.google.gson.Gson;
import org.apache.jena.rdf.model.RDFNode;

import java.util.Objects;

/**
 * This interface defines the base methods that manages the mapping of any
 * source entity to their respective rdf term through the use of a column
 * to locate the term value.
 */
@Deprecated
public class ColumnTermMap implements TermMap {

    private final String columnName;
    private TermType termType = TermType.LITERAL;

    /**
     * Constructs a ColumnTermMap with the specified column name and
     * term type to map into.
     *
     * @param columnName the column name to locate the value
     * @param termType   the term type to map the value into
     */
    protected ColumnTermMap(String columnName, TermType termType) {
        this.columnName = Objects.requireNonNull(columnName, "Column name must not be null.");
        Objects.requireNonNull(termType, "Term type must not be null.");
        if (termType != TermType.UNDEFINED) {
            this.termType = termType;
        }
    }

    @Override
    public RDFNode generateRDFTerm(Entity entity) {
        String value = entity.getPropertyValue(columnName);
        return value == null ? null : RDFTermHelper.asRDFTerm(value, termType);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
