/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Collection of {@code Record} as EntityRecord.
 * <p>
 * Note: This class assumes that the source using this is a structured source that
 * will generate each {@code Record} with the same properties as all other within
 * this instance.
 */
public class MutableEntityRecord implements EntityRecord {

    private final Set<Record> records = new LinkedHashSet<>();
    private int columnSize;

    @Override
    public int columnSize() {
        return columnSize;
    }

    @Override
    public int size() {
        return records.size();
    }

    @Override
    public boolean containsRecord(Record record) {
        return records.contains(record);
    }

    @Override
    public Iterator<Record> iterator() {
        return records.iterator();
    }

    @Override
    public void clear() {
        records.clear();
    }

    @Override
    public boolean removeRecord(Record record) {
        return records.remove(record);
    }

    @Override
    public boolean addRecord(Record record) {
        columnSize = record.columnSize();
        Iterator<Record> iterator = records.iterator();
        if (iterator.hasNext()) {
            Record thisRecord = iterator.next();
            if (!thisRecord.properties().equals(record.properties())) return false;
        }

        return records.add(record);
    }

    @Override
    public boolean isEmpty() {
        return records.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MutableEntityRecord records1 = (MutableEntityRecord) obj;
        return Objects.equals(records, records1.records);
    }

    @Override
    public int hashCode() {
        return Objects.hash(records);
    }
}