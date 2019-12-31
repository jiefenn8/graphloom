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
import com.google.common.collect.ImmutableSet;
import org.apache.jena.rdf.model.RDFNode;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML RefObjectMap with {@link NodeMap} interface.
 * This class is an immutable class and require the use of its {@link Builder}
 * class to populate and create an instance.
 */
public class RefObjectMap implements NodeMap, EntityChild {

    private final TriplesMap parent;
    private final TriplesMap parentTriplesMap;
    private final Set<JoinCondition> joinConditions;

    /**
     * Constructs a RefObjectMap with the specified Builder containing the
     * properties to populate and initialise an immutable instance.
     *
     * @param builder the ref object map builder to build from
     */
    private RefObjectMap(Builder builder) {
        checkNotNull(builder);
        parentTriplesMap = builder.parentTriplesMap;
        joinConditions = ImmutableSet.copyOf(builder.joinConditions);
        parent = builder.parent;
    }

    /**
     * Returns the triples map that this ref object map referenced to generate
     * the object term.
     *
     * @return the triples map referenced
     */
    public TriplesMap getParentTriplesMap() {
        return parentTriplesMap;
    }

    /**
     * Returns true if this ref object map contains any join conditions.
     *
     * @return true if there is any join conditions
     */
    public boolean hasJoinCondition() {
        return !joinConditions.isEmpty();
    }

    /**
     * Returns a set of all join conditions in this ref object map.
     *
     * @return set of all join conditions
     */
    public Set<JoinCondition> listJoinConditions() {
        return joinConditions;
    }

    /**
     * Returns true if the queries of the logical tables from both the parent
     * triples map and the triples map that called this is equal.
     *
     * @param logicalTable the logical table of the calling triples map
     * @return true if queries from both are equal
     */
    protected boolean isQueryEqual(LogicalTable logicalTable) {
        return this.parentTriplesMap.isQueryEqual(logicalTable);
    }

    @Override
    public EntityMap getEntityMap() {
        return parent;
    }

    @Override
    public RDFNode generateNodeTerm(Record r) {
        RDFNode term = parentTriplesMap.generateEntityTerm(r);
        if (term.isLiteral()) {
            throw new MapperException("RefObjectMap should only return IRI.");
        }
        return term.asResource();
    }

    /**
     * Builder class for RefObjectMap.
     */
    public static class Builder {

        private final TriplesMap parentTriplesMap;
        private final Set<JoinCondition> joinConditions = new HashSet<>();
        private TriplesMap parent;

        /**
         * Constructs a RefObjectMap Builder with the specified triples map
         * instance.
         *
         * @param parentTriplesMap the triples map to reference
         */
        public Builder(TriplesMap parentTriplesMap) {
            this.parentTriplesMap = parentTriplesMap;
        }

        /**
         * Adds association to an triples map that this ref object map
         * belongs to.
         *
         * @param triplesMap the triples map to associate with
         * @return this builder for fluent method chaining
         */
        public Builder withTriplesMap(TriplesMap triplesMap) {
            parent = triplesMap;
            return this;
        }

        /**
         * Adds a join condition to this reference object map that associates
         * two queries from the logical tables of both parent triples map and
         * this triples map (that accessed this ref object map) by the given
         * columns that exists in their table.
         *
         * @param parent the column in the parent triples maps's logical table
         * @param child  the column in this triples map's logical table
         * @return this builder for fluent method chaining
         */
        public Builder addJoinCondition(String parent, String child) {
            joinConditions.add(new JoinCondition(parent, child));
            return this;
        }

        /**
         * Returns an immutable instance of ref object map containing the
         * properties given to its builder.
         *
         * @return the ref object map created with this builder parameters
         */
        public RefObjectMap build() {
            return new RefObjectMap(this);
        }
    }
}
