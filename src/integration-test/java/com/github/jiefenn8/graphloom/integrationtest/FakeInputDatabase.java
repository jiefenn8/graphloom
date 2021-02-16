/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.integrationtest;

import com.github.jiefenn8.graphloom.api.EntityReference;
import com.github.jiefenn8.graphloom.api.InputSource;
import com.github.jiefenn8.graphloom.api.inputsource.Entity;
import com.github.jiefenn8.graphloom.api.inputsource.EntityResult;
import com.github.jiefenn8.graphloom.integrationtest.fake.FakeEntity;
import com.github.jiefenn8.graphloom.integrationtest.fake.FakeEntityResult;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Fake class simulating InputSource implementation of a database.
 */
public class FakeInputDatabase implements InputSource {

    private EntityResult getEMPEntityResults() {
        Map<String, String> record = Map.of(
                "EMPNO", "7369",
                "ENAME", "SMITH",
                "JOB", "CLERK",
                "DEPTNO", "10"
        );
        Entity fakeEntity = new FakeEntity(record);
        return new FakeEntityResult(Set.of(fakeEntity));
    }

    private EntityResult getDEPTEntityResults() {
        Map<String, String> record = Map.of(
                "DEPTNO", "10",
                "DNAME", "APPSERVER",
                "LOC", "NEW YORK"
        );
        Entity fakeEntity = new FakeEntity(record);
        return new FakeEntityResult(Set.of(fakeEntity));
    }

    @Override
    public void executeEntityQuery(EntityReference entityRef, Consumer<EntityResult> action) {
        String payload = entityRef.getPayload();
        if (payload.equals("DEPT")) {
            action.accept(getDEPTEntityResults());
        } else if (payload.equals("EMP")) {
            action.accept(getEMPEntityResults());
        } else {
            action.accept(new FakeEntityResult(new HashSet<>()));
        }
    }
}
