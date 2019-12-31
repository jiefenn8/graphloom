/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.*;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import com.google.common.base.Preconditions;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML LogicalTable with {@link SourceMap} interface.
 */
public class LogicalTable implements SourceMap, EntityChild {

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalTable that = (LogicalTable) o;
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
    public SourceMap loadInputSource(InputSource s) {
        inputSource = s;
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

        public Builder(SourceConfig sourceConfig) {
            this.sourceConfig = checkNotNull(sourceConfig, "Payload must not be null.");
        }

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
         * Builds a source config with a join SQL query consisting of two query,
         * table or mixed that is associated to each other through join
         * conditions.
         *
         * @param logicalTable   the second query or table to build a joint SQL
         * @param joinConditions the set of joins conditions to use
         * @return this builder for fluent method chaining
         */
        public Builder withJointSQLQuery(LogicalTable logicalTable, Set<JoinCondition> joinConditions) {
            if (joinConditions.isEmpty()) {
                throw new MapperException("Expected JoinConditions with joint SQL query creation.");
            }

            String parent = prepareSQLQuery(logicalTable.sourceConfig);
            String child = prepareSQLQuery(sourceConfig);
            String joins = buildJoinStatement(joinConditions.iterator());
            String jointSQLQuery = String.format("SELECT * FROM %s AS q1, %s as q2 WHERE %s", child, parent, joins);

            String parentSqlVersion = sourceConfig.getProperty("sqlVersion");
            this.sourceConfig = R2RMLFactory.createR2RMLView(jointSQLQuery, parentSqlVersion);
            return this;
        }

        /**
         * Returns the ending SQL query segment containing all the join conditions
         * recursively built from the given iterator of a join condition collection.
         *
         * @param iterator of the join condition collection
         * @return the ending segment containing SQL built join conditions
         */
        private String buildJoinStatement(Iterator<JoinCondition> iterator) {
            JoinCondition join = iterator.next();
            String joinStatement = String.format("q1.%s=q2.%s", join.getChild(), join.getParent());
            if (iterator.hasNext()) {
                joinStatement = joinStatement.concat(" AND " + buildJoinStatement(iterator));
            }

            return joinStatement.concat(";");
        }

        /**
         * Returns a prepared SQL query using the query/table in the given source
         * config. If the source config is a r2rml view, wrap the query before
         * returning it.
         *
         * @param sourceConfig the config to containing the query
         * @return the query prepared for further manipulation
         */
        private String prepareSQLQuery(SourceConfig sourceConfig) {
            if (sourceConfig instanceof R2RMLView) {
                return String.format("(%s)", sourceConfig.getPayload());
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
