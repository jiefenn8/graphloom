/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
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

    private Set<Record> records;

    //Default constructor
    public MutableEntityRecord() {
        records = new LinkedHashSet<>();
    }

    @Override
    public int size() {
        return records.size();
    }

    @Override
    public boolean containsRecord(Record r) {
        return records.contains(r);
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
    public boolean removeRecord(Record r) {
        return records.remove(r);
    }

    @Override
    public boolean addRecord(Record r) {
        Iterator<Record> iterator = records.iterator();
        if (iterator.hasNext()) {
            Record record = iterator.next();
            if (!record.properties().equals(r.properties())) return false;
        }

        return records.add(r);
    }

    @Override
    public boolean isEmpty() {
        return records.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableEntityRecord records1 = (MutableEntityRecord) o;
        return Objects.equals(records, records1.records);
    }

    @Override
    public int hashCode() {
        return Objects.hash(records);
    }
}