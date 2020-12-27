/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.*;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import com.google.common.base.Preconditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML LogicalTable with {@link SourceMap} interface.
 */
public class LogicalTable implements SourceMap, EntityChild {

    private static final Logger logger = LogManager.getLogger();

    private final TriplesMap parent;
    private final SourceConfig sourceConfig;
    private InputSource inputSource;

    /**
     * Constructs a LogicalTable with the specified Builder containing the
     * properties to populate and initialise an immutable instance.
     *
     * @param builder the logical table builder to build from
     */
    private LogicalTable(Builder builder) {
        checkNotNull(builder);
        sourceConfig = builder.sourceConfig;
        parent = builder.parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LogicalTable that = (LogicalTable) obj;
        return Objects.equals(sourceConfig, that.sourceConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceConfig);
    }

    @Override
    public EntityRecord getEntityRecord(int batchId) {
        return inputSource.getEntityRecord(sourceConfig, batchId);
    }

    @Override
    public SourceMap loadInputSource(InputSource inputSource) {
        this.inputSource = inputSource;
        return this;
    }

    @Override
    public void forEachEntityRecord(Consumer<Record> action) {
        Preconditions.checkNotNull(action);
        int totalBatch = inputSource.calculateNumOfBatches(sourceConfig);
        for (int batchId = 0; batchId < totalBatch; batchId++) {
            EntityRecord entityRecord = getEntityRecord(batchId);
            for (Record record : entityRecord) {
                action.accept(record);
            }
        }
    }

    @Override
    public EntityMap getEntityMap() {
        return parent;
    }

    /**
     * Builder class for LogicalTable.
     */
    public static class Builder {

        private SourceConfig sourceConfig;
        private TriplesMap parent;

        /**
         * Constructs a Builder with the specified SourceConfig instance.
         *
         * @param sourceConfig the source config to set on this logical table
         */
        public Builder(SourceConfig sourceConfig) {
            this.sourceConfig = checkNotNull(sourceConfig, "Payload must not be null.");
        }

        /**
         * Constructs a Builder with the specified LogicalTable containing the
         * source config instance.
         *
         * @param logicalTable the logical table with the source config needed
         *                     to set on this logical table
         */
        public Builder(LogicalTable logicalTable) {
            this.sourceConfig = checkNotNull(logicalTable.sourceConfig, "Payload must not be null.");
        }

        /**
         * Adds association to an triples map that this logical table belongs to.
         *
         * @param triplesMap the triples map to associate with
         * @return this builder for fluent method chaining
         */
        protected Builder withTriplesMap(TriplesMap triplesMap) {
            parent = triplesMap;
            return this;
        }

        /**
         * Builds a source config with a join query consisting of two query,
         * table or mixed that is associated to each other through join
         * conditions.
         *
         * @param logicalTable   the second query or table to build a joint SQL
         * @param joinConditions the set of joins conditions to use
         * @return this builder for fluent method chaining
         */
        public Builder withJointSQLQuery(LogicalTable logicalTable, Set<JoinCondition> joinConditions) {
            if (joinConditions.isEmpty()) {
                String message = "Expected JoinConditions with joint SQL query creation.";
                logger.fatal(message);
                throw new MapperException(message);
            }

            String jointQuery = "SELECT " + buildSelective(joinConditions.iterator());
            jointQuery += " FROM " + prepareQuery(sourceConfig) + " AS child, ";
            jointQuery += prepareQuery(logicalTable.sourceConfig) + " AS parent";
            jointQuery += " WHERE " + buildJoinStatement(joinConditions.iterator());
            String parentVersion = sourceConfig.getProperty("sqlVersion");
            this.sourceConfig = R2RMLFactory.createR2RMLView(jointQuery, parentVersion);
            return this;
        }

        /**
         * Returns the starting select query segment containing all the join
         * conditions recursively added from the {@link JoinCondition} iterator.
         *
         * @param iterator of the join condition collection
         * @return the starting select segment containing the join conditions
         */
        private String buildSelective(Iterator<JoinCondition> iterator) {
            JoinCondition join = iterator.next();
            String selective = "child." + join.getChild() + ", parent." + join.getParent();
            if (iterator.hasNext()) {
                selective += ", " + buildSelective(iterator);
            }
            return selective;
        }

        /**
         * Returns the ending query segment containing all the join conditions
         * recursively built from the given iterator of a join condition collection.
         *
         * @param iterator of the join condition collection
         * @return the ending segment containing SQL built join conditions
         */
        private String buildJoinStatement(Iterator<JoinCondition> iterator) {
            JoinCondition join = iterator.next();
            String joinStatement = "child." + join.getChild() + "=parent." + join.getParent();
            if (iterator.hasNext()) {
                joinStatement = joinStatement.concat(" AND " + buildJoinStatement(iterator));
            }
            return joinStatement;
        }

        /**
         * Returns a prepared query using the query/table in the given source
         * config. If the source config is a r2rml view, wrap the query before
         * returning it.
         *
         * @param sourceConfig the config to containing the query
         * @return the query prepared for further manipulation
         */
        private String prepareQuery(SourceConfig sourceConfig) {
            if (sourceConfig instanceof R2RMLView) {
                return "(" + sourceConfig.getPayload() + ")";
            }
            return sourceConfig.getPayload();
        }

        /**
         * Returns an immutable instance of logical table containing the properties
         * given to its builder.
         *
         * @return instance of logical table created with the info in this builder
         */
        public LogicalTable build() {
            return new LogicalTable(this);
        }
    }

}
