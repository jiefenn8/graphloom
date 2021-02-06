/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.*;
import com.github.jiefenn8.graphloom.api.inputsource.Entity;
import com.github.jiefenn8.graphloom.exceptions.ParserException;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.Resource;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML TriplesMap with {@link EntityMap} interface.
 * This class is an immutable class and requires the use of its {@link Builder}
 * class to populate and create an instance.
 */
public class TriplesMap implements EntityMap {

    private final String idName;
    private final LogicalTable logicalTable;
    private final SubjectMap subjectMap;
    private final Map<RelationMap, NodeMap> predicateObjectMaps;

    /**
     * Constructs a TriplesMap with the specified Builder containing the
     * properties to populate and initialise this immutable instance.
     *
     * @param builder the triples map builder to build from
     */
    private TriplesMap(Builder builder) {
        checkNotNull(builder);
        idName = builder.idName;
        logicalTable = builder.logicalTable;
        subjectMap = builder.subjectMap.withParentMap(this);
        predicateObjectMaps = ImmutableMap.copyOf(builder.predicateObjectMaps);
    }

    /**
     * Returns true if this instance logical table query config is equal to the
     * given logical table.
     *
     * @param logicalTable logical table to compare
     * @return true if logical table query is equal
     */
    protected boolean isQueryEqual(LogicalTable logicalTable) {
        return this.logicalTable.equals(logicalTable);
    }

    @Override
    public SourceMap applySource(InputSource inputSource) {
        return logicalTable.loadInputSource(inputSource);
    }

    @Override
    public SourceMap getSourceMap() {
        return logicalTable;
    }

    @Override
    public String getIdName() {
        return idName;
    }

    @Override
    public Set<RelationMap> listRelationMaps() {
        return Collections.unmodifiableSet(predicateObjectMaps.keySet());
    }

    @Override
    public NodeMap getNodeMapWithRelation(RelationMap relationMap) {
        return predicateObjectMaps.get(relationMap);
    }

    @Override
    public boolean hasRelationNodeMaps() {
        return !predicateObjectMaps.isEmpty();
    }

    @Override
    public Resource generateEntityTerm(Entity entity) {
        return subjectMap.generateEntityTerm(entity);
    }

    public Resource generateEntityTerm(Set<JoinCondition> joins, Entity entity) {
        return subjectMap.generateEntityTerm(joins, entity);
    }

    @Override
    public List<Resource> listEntityClasses() {
        return subjectMap.listEntityClasses();
    }

    /**
     * Builder class for TriplesMap.
     */
    public static class Builder {

        private final String idName;
        private final LogicalTable logicalTable;
        private final SubjectMap subjectMap;
        private final Map<RelationMap, NodeMap> predicateObjectMaps = new HashMap<>();

        /**
         * Constructs a TriplesMap Builder with the specified triples map id,
         * logical table and subject map instance.
         *
         * @param idName       the name of this triples map definition
         * @param logicalTable the logical table to set on this triples map
         * @param subjectMap   the subject map to set on this triples map
         */
        public Builder(String idName, LogicalTable logicalTable, SubjectMap subjectMap) {
            this.idName = checkNotNull(idName, "ID name must not be null.");
            this.logicalTable = checkNotNull(logicalTable, "Logical table must not be null.");
            this.subjectMap = checkNotNull(subjectMap, "Subject map must not be null.");
        }

        /**
         * Adds a predicate map and object map pair to this triples map.
         *
         * @param pom the pair to add to triples map
         * @return this builder for fluent method chaining
         */
        public Builder addPredicateObjectMap(Pair<PredicateMap, NodeMap> pom) {
            NodeMap nodeMap = pom.getValue();
            if (nodeMap instanceof RefObjectMap) {
                RefObjectMap refObjectMap = (RefObjectMap) nodeMap;
                if (!refObjectMap.isQueryEqual(logicalTable) && !refObjectMap.hasJoinCondition()) {
                    throw new ParserException("Triples Maps queries do not match. Must provide join condition.");
                }
            }
            predicateObjectMaps.put(pom.getKey(), pom.getValue());
            return this;
        }

        /**
         * Returns an immutable instance of triples map containing the properties
         * given to its builder.
         *
         * @return instance of triples map created with the info in this builder
         */
        public TriplesMap build() {
            return new TriplesMap(this);
        }
    }
}
