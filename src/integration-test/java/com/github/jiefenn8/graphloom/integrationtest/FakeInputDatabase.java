/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.integrationtest;

import com.github.jiefenn8.graphloom.api.*;
import com.google.common.collect.ImmutableMap;

/**
 * Fake class simulating InputSource implementation of a database.
 */
public class FakeInputDatabase implements InputSource {

    @Override
    public EntityRecord getEntityRecord(EntityReference sourceConfig, int batchId) {
        //Ignore batchId since this is a single entry table
        //Return EMP record as default
        String payload = sourceConfig.getPayload();
        if (payload.equals("DEPT")) return getDeptRecords();

        return getEmpRecords();
    }

    /**
     * Returns the entity record of a predefined employee table.
     *
     * @return the entity record of employee records.
     */
    private EntityRecord getEmpRecords() {
        Record record1 = new MutableRecord(ImmutableMap.of(
                "EMPNO", "7369",
                "ENAME", "SMITH",
                "JOB", "CLERK",
                "DEPTNO", "10"));

        MutableEntityRecord entityRecord = new MutableEntityRecord();
        entityRecord.addRecord(record1);

        return entityRecord;
    }

    /**
     * Returns the entity record of a predefined department table.
     *
     * @return the entity record of department records.
     */
    private EntityRecord getDeptRecords() {
        Record record1 = new MutableRecord(ImmutableMap.of(
                "DEPTNO", "10",
                "DNAME", "APPSERVER",
                "LOC", "NEW YORK"
        ));

        MutableEntityRecord entityRecord = new MutableEntityRecord();
        entityRecord.addRecord(record1);

        return entityRecord;
    }

    @Override
    public int calculateNumOfBatches(EntityReference sourceConfig) {
        return 1;
    }
}
