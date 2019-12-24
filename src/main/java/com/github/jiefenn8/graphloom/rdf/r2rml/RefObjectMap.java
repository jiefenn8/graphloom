/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.EntityChild;
import com.github.jiefenn8.graphloom.api.EntityMap;
import com.github.jiefenn8.graphloom.api.NodeMap;
import com.github.jiefenn8.graphloom.api.Record;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.RDFNode;

public class RefObjectMap implements NodeMap, EntityChild {

    private final TriplesMap triplesMapRef;
    private final String childColumn;
    private final String parentColumn;
    private TriplesMap parent;

    protected RefObjectMap(Builder builder) {
        triplesMapRef = builder.triplesMapRef;
        parent = builder.parent;
        childColumn = builder.childQuery;
        parentColumn = builder.parentQuery;
    }

    protected boolean isQueryEqual(LogicalTable logicalTable) {
        return this.triplesMapRef.isQueryEqual(logicalTable);
    }

    @Override
    public EntityMap getParentMap() {
        return parent;
    }

    @Override
    public RDFNode generateNodeTerm(Record r) {
        RDFNode term = triplesMapRef.generateEntityTerm(r);
        if (term.isLiteral()) {
            throw new MapperException("RefObjectMap should only return IRI.");
        }

        return term.asResource();
    }

    public static class Builder {

        private final TriplesMap triplesMapRef;
        private TriplesMap parent;
        private String parentQuery = StringUtils.EMPTY;
        private String childQuery = StringUtils.EMPTY;


        public Builder(TriplesMap triplesMapRef) {
            this.triplesMapRef = triplesMapRef;
        }

        public Builder withParentMap(TriplesMap triplesMap) {
            parent = triplesMap;
            return this;
        }

        public Builder setJoinCondition(String parent, String child) {
            parentQuery = parent;
            childQuery = child;
            return this;
        }

        public RefObjectMap build() {
            return new RefObjectMap(this);
        }
    }
}
