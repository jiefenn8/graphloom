/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.Record;
import org.apache.jena.rdf.model.RDFNode;

import static com.google.common.base.Preconditions.checkNotNull;

public class ColumnTermMap implements TermMap {

    private String columnName;
    private TermType termType;

    protected ColumnTermMap(String columnName, TermType t) {
        this.columnName = checkNotNull(columnName, "Column name must not be null.");
        termType = checkNotNull(t, "Term type must not be null.");
    }

    @Override
    public RDFNode generateRDFTerm(Record r) {
        String columnValue = r.getPropertyValue(columnName);

        return RDFTermHelper.asRDFTerm(columnValue, termType);
    }
}
