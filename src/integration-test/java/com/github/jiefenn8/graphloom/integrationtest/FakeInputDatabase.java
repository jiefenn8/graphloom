/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.integrationtest;

import com.github.jiefenn8.graphloom.api.InputSource;
import com.github.jiefenn8.graphloom.api.MutableEntityRecord;
import com.github.jiefenn8.graphloom.api.MutableRecord;
import com.github.jiefenn8.graphloom.api.SourceConfig;
import com.google.common.collect.ImmutableMap;

public class FakeInputDatabase implements InputSource {

    @Override
    public MutableEntityRecord getEntityRecord(SourceConfig config, int batchId) {
        MutableEntityRecord entityRecord = new MutableEntityRecord();
        entityRecord.addRecord(new MutableRecord(ImmutableMap.of(
                "EMPNO", "7369",
                "ENAME", "SMITH",
                "JOB", "CLERK",
                "DEPTNO", "10")));

        return entityRecord;
    }

    @Override
    public int calculateNumOfBatches(SourceConfig config) {
        return 1;
    }
}
