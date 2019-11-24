/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents a single record of an Entity using HashMap implementation with the
 * property as String key its value as String value.
 */
public class MutableRecord implements Record {

    private Map<String, String> recordValue;

    //Default constructor
    public MutableRecord(String column, String value) {
        recordValue = new HashMap<>();
        recordValue.put(column, value);
    }

    public MutableRecord(Map<String, String> m) {
        recordValue = new HashMap<>(m);
    }

    @Override
    public String addProperty(String property, String value) {
        return recordValue.put(property, value);
    }

    @Override
    public boolean hasProperty(String property) {
        return recordValue.containsKey(property);
    }

    @Override
    public boolean hasValue(String value) {
        return recordValue.containsValue(value);
    }

    @Override
    public String getPropertyValue(String property) {
        return recordValue.get(property);
    }

    @Override
    public String removeProperty(String property) {
        return recordValue.remove(property);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableRecord that = (MutableRecord) o;
        return Objects.equals(recordValue, that.recordValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordValue);
    }

    @Override
    public Set<String> properties() {
        return recordValue.keySet();
    }
}
