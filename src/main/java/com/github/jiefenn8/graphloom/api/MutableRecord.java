/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import java.util.*;

/**
 * This class represents a single record of an Entity using HashMap
 * implementation with the property as String key its value as String
 * value.
 */
public class MutableRecord implements Record {

    private Map<String, String> recordValue;

    /**
     * Constructs a MutableRecord with an initial column and its value.
     *
     * @param column the name of the column
     * @param value  the value associated with the column
     */
    public MutableRecord(String column, String value) {
        recordValue = new LinkedHashMap<>();
        recordValue.put(column, value);
    }

    /**
     * Constructs a MutableRecord with the specified map containing all
     * the properties and their values to add into this record.
     *
     * @param columns the map with all the properties and their values to add
     */
    public MutableRecord(Map<String, String> columns) {
        recordValue = new HashMap<>(columns);
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MutableRecord that = (MutableRecord) obj;
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

    @Override
    public int columnSize() {
        return recordValue.size();
    }
}
