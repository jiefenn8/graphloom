/*
 *    Copyright (c) 2019 - Javen Liu (github.com/jiefenn8)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
public class LinkedHashEntityRecord implements EntityRecord {

    private Set<Record> records;

    //Default constructor
    public LinkedHashEntityRecord() {
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
        LinkedHashEntityRecord records1 = (LinkedHashEntityRecord) o;
        return Objects.equals(records, records1.records);
    }

    @Override
    public int hashCode() {
        return Objects.hash(records);
    }
}