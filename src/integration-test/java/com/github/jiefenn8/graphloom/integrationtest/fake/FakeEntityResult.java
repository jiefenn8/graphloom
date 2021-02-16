/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.integrationtest.fake;

import com.github.jiefenn8.graphloom.api.inputsource.Entity;
import com.github.jiefenn8.graphloom.api.inputsource.EntityResult;

import java.util.Iterator;
import java.util.Set;

/**
 * Fake class simulating EntityResult implementation of a result collection
 * after execution of a query.
 */
public class FakeEntityResult implements EntityResult {

    private final Iterator<Entity> iter;

    public FakeEntityResult(Set<Entity> records) {
        this.iter = records.iterator();
    }

    @Override
    public boolean hasNext() {
        return iter.hasNext();
    }

    @Override
    public Entity nextEntity() {
        return iter.next();
    }
}
