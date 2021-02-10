/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.integrationtest.fake;

import com.github.jiefenn8.graphloom.api.inputsource.Entity;

import java.util.Map;

/**
 * Fake class simulating an Entity implementation of a result entity.
 */
public class FakeEntity implements Entity {

    private final Map<String, String> properties;

    public FakeEntity(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String getPropertyValue(String name) {
        return properties.get(name);
    }
}
