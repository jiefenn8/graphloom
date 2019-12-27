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

    private EntityMap parent;
    private SourceConfig sourceConfig;
    private InputSource inputSource;

    protected LogicalTable(SourceConfig sourceConfig) {
        this.sourceConfig = checkNotNull(sourceConfig, "Payload must not be null.");
    }

    public LogicalTable withJointSQLQuery(LogicalTable logicalTable, Set<JoinCondition> joins) {
        if (joins.isEmpty()) {
            throw new MapperException("Expected joinCondition.");
        }

        String parentQuery = prepareSQLQuery(logicalTable);
        String childQuery = prepareSQLQuery(this);
        String jointSQL = String.format("SELECT * FROM %s AS q1, %s as q2 WHERE ", childQuery, parentQuery);
        Iterator<JoinCondition> joinIterator = joins.iterator();
        while (joinIterator.hasNext()) {
            JoinCondition join = joinIterator.next();
            jointSQL = jointSQL.concat(generateSQLJoin(join));
            if (joinIterator.hasNext()) {
                jointSQL = jointSQL.concat(" AND ");
            }
            if (!joinIterator.hasNext()) {
                jointSQL = jointSQL.concat(";");
            }
        }

        return R2RMLFactory.createR2RMLView(jointSQL, sourceConfig.getProperty("sqlVersion"));
    }

    private String prepareSQLQuery(LogicalTable logicalTable) {
        SourceConfig sourceConfig = logicalTable.sourceConfig;
        if (sourceConfig instanceof R2RMLView) {
            return String.format("(%s)", sourceConfig.getPayload());
        }
        return sourceConfig.getPayload();
    }

    private String generateSQLJoin(JoinCondition join) {
        return String.format("q1.%s=q2.%s", join.getChild(), join.getParent());
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

    protected LogicalTable withParentMap(EntityMap em) {
        parent = em;
        return this;
    }

    @Override
    public EntityMap getEntityMap() {
        return parent;
    }
}
