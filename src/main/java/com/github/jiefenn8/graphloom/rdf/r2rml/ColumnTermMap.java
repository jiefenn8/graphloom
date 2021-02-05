/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.Record;
import com.github.jiefenn8.graphloom.api.inputsource.Entity;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.RDFNode;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This interface defines the base methods that manages the mapping of any
 * source record to their respective rdf term through the use of a column
 * to locate the term value.
 */
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
        this.columnName = checkNotNull(columnName, "Column name must not be null.");
        checkNotNull(termType, "Term type must not be null.");
        if (termType != TermType.UNDEFINED) {
            this.termType = termType;
        }
    }

    @Override
    public RDFNode generateRDFTerm(Record record) {
        String value = record.getPropertyValue(columnName);
        return value == null ? null : RDFTermHelper.asRDFTerm(value, termType);
    }

    @Override
    public RDFNode generateRDFTerm(Entity entity) {
        String value = entity.getPropertyValue(columnName);
        return value == null ? null : RDFTermHelper.asRDFTerm(value, termType);
    }

    @Override
    public RDFNode generateRDFTerm(Set<JoinCondition> joins, Record record) {
        String alt = StringUtils.EMPTY;
        for (JoinCondition join : joins) {
            if (join.getParent().equals(columnName)) {
                alt = join.getChild();
            }
        }

        String value = record.getPropertyValue(alt);
        return value == null ? null : RDFTermHelper.asRDFTerm(value, termType);
    }

    @Override
    public RDFNode generateRDFTerm(Set<JoinCondition> joins, Entity entity) {
        String alt = StringUtils.EMPTY;
        for (JoinCondition join : joins) {
            if (join.getParent().equals(columnName)) {
                alt = join.getChild();
            }
        }

        String value = entity.getPropertyValue(alt);
        return value == null ? null : RDFTermHelper.asRDFTerm(value, termType);
    }
}
